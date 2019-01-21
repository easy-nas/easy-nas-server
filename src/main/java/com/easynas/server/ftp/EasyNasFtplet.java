package com.easynas.server.ftp;

import com.easynas.server.service.FileService;
import com.easynas.server.util.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author liangyongrui
 */
@Component
@Slf4j
public class EasyNasFtplet extends DefaultFtplet {

    @Autowired
    private FileService fileService;

    /**
     * 上传结束 检查和转移文件，并创建同名软链接
     */
    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) throws IOException {
        final var path = session.getUser().getHomeDirectory() + request.getArgument();
        final var sha256sum = CommandUtils.sha256sum(path);
        final var fileSavePath = fileService.saveFile(path, sha256sum);
        final var exec = CommandUtils.lnS(fileSavePath, path);
        try {
            exec.waitFor();
        } catch (InterruptedException e) {
            log.error("建立软链接失败", e);
        }
        return FtpletResult.DEFAULT;
    }

    @Override
    public FtpletResult onLogin(FtpSession session, FtpRequest request) {
        final var user = session.getUser();
        if (user != null) {
            final var homeDirectory = user.getHomeDirectory();
            final var file = new File(homeDirectory);
            if (!file.exists() && !file.mkdirs()) {
                return FtpletResult.SKIP;
            }
        }
        return FtpletResult.DEFAULT;
    }

}
