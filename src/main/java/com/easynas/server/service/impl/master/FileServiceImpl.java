package com.easynas.server.service.impl.master;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.service.base.BaseFileService;
import com.easynas.server.service.FileService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-1-22 下午4:39
 */
@Service("fileService")
public class FileServiceImpl extends BaseFileService implements FileService {

    @Autowired
    public FileServiceImpl(@Qualifier("configDao") @NonNull ConfigDao configDao) {
        super(configDao);
    }

    @Override
    public String saveFile(String fileName, String path) {
        return getFilePath(fileName).orElseGet(() -> saveNewFile(fileName, path));
    }

    @Override
    public List<String> getFileSavePaths() {
        return configDao.getFileSavePaths();
    }

    @Override
    public void setFileSavePath(@NonNull List<String> path) {
        configDao.setFileSavePath(path);
    }
}
