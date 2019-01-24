package com.easynas.server.ftp;

import com.easynas.server.config.GlobalStatus;
import com.easynas.server.service.FileService;
import com.easynas.server.util.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ftplet.*;
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
        final var fileSavePath = fileService.saveFile(sha256sum, path);
        final var exec = CommandUtils.lnS(fileSavePath, path);
        try {
            exec.waitFor();
        } catch (InterruptedException e) {
            log.error("建立软链接失败", e);
        }
        GlobalStatus.uploadCountDecrement();
        return FtpletResult.DEFAULT;
    }

    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest request) {
        GlobalStatus.uploadCountIncrement();
        return FtpletResult.DEFAULT;
    }

    @Override
    public FtpletResult onLogin(FtpSession session, FtpRequest request) {
        final var user = session.getUser();
        if (user != null) {
            final var homeDirectory = user.getHomeDirectory();
            final var file = new File(homeDirectory);
            if (!file.exists() && !file.mkdirs()) {
                return FtpletResult.DISCONNECT;
            }
        }
        return FtpletResult.DEFAULT;
    }

    @Override
    public FtpletResult beforeCommand(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        if (GlobalStatus.getLock()) {
            return FtpletResult.DISCONNECT;
        }
        return super.beforeCommand(session, request);
    }

}
