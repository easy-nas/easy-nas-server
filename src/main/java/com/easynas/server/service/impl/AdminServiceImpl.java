package com.easynas.server.service.impl;

import com.easynas.server.config.GlobalStatus;
import com.easynas.server.db.ConfigDb;
import com.easynas.server.db.UserDb;
import com.easynas.server.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author liangyongrui
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final ConfigDb configDb;
    private final UserDb userDb;

    @Autowired
    public AdminServiceImpl(ConfigDb configDb, UserDb userDb) {
        this.configDb = configDb;
        this.userDb = userDb;
    }

    @Override
    public String setGeneralInformationPath(String path) {
        File file = new File(path);
        if (file.exists()) {
            return "该路径已存在！";
        }
        String oldPath = configDb.getGeneralInformationPath();
        if (oldPath != null) {
            boolean mvStatus = mv(oldPath, path, configDb::setGeneralInformationPath);
            if (!mvStatus) {
                return "移动历史文件失败";
            }
        } else {
            configDb.setGeneralInformationPath(path);
        }
        userDb.initAllUser();
        return null;
    }

    @Override
    public String setBackupGeneralInformationPath(String path) {
        File file = new File(path);
        if (file.exists()) {
            return "该路径已存在！";
        }
        String oldPath = configDb.getBackupGeneralInformationPath();
        if (oldPath != null) {
            boolean mvStatus = mv(oldPath, path, configDb::setBackupGeneralInformationPath);
            if (!mvStatus) {
                return "移动历史文件失败";
            }
        } else {
            configDb.setBackupGeneralInformationPath(path);
        }
        userDb.initAllUser();
        return null;
    }

    private boolean mv(String source, String destination, Consumer<String> destinationConsumer) {
        try {
            GlobalStatus.setLock(true);
            Process exec = Runtime.getRuntime().exec("cp " + source + " " + destination);
            exec.waitFor();
            destinationConsumer.accept(destination);
            GlobalStatus.setLock(false);
            Runtime.getRuntime().exec("rm -rf " + source);
            return true;
        } catch (IOException | InterruptedException e) {
            log.error("移动文件失败，source: " + source + ", destination: " + destination, e);
            return false;
        }
    }
}
