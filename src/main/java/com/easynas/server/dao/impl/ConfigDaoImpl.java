package com.easynas.server.dao.impl;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.db.ConfigDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @author liangyongrui
 */
@Repository
public class ConfigDaoImpl implements ConfigDao {

    private final ConfigDb configDb;

    @Autowired
    public ConfigDaoImpl(ConfigDb configDb) {
        this.configDb = configDb;
    }

    /**
     * 用户数据保存路径
     */
    @Override
    public String getUserDataSavePath(String username) {
        return configDb.getGeneralInformationPath() + "/" + username + "/data";
    }

    @Override
    public String getGeneralInformationPath() {
        return configDb.getGeneralInformationPath();
    }

    @Override
    public void setGeneralInformationPath(String s) {
        configDb.setGeneralInformationPath(s);
    }

    @Override
    public Optional<String> getGeneralInformationPathBackup() {
        return configDb.getGeneralInformationPathBackup();
    }

    @Override
    public void setGeneralInformationPathBackup(String path) {
        configDb.setGeneralInformationPathBackup(path);
    }

    @Override
    public List<String> getFileSavePaths() {
        return configDb.getFileSavePaths();
    }

    @Override
    public void setFileSavePath(List<String> path) {
        configDb.setFileSavePath(path);
    }

    @Override
    public List<String> getFileSavePathsBackup() {
        return configDb.getFileSavePathsBackup();
    }

    @Override
    public void setFileSavePathBackup(List<String> path) {
        configDb.setFileSavePathBackup(path);
    }

    @Override
    public String getMaxFreeSpacePath() {
        return configDb.getFileSavePaths().stream().max((o1, o2) -> {
            final var sub = new File(o1).getFreeSpace() - new File(o2).getFreeSpace();
            return sub == 0L ? 0 : (sub > 0 ? 1 : -1);
        }).orElseThrow();
    }
}
