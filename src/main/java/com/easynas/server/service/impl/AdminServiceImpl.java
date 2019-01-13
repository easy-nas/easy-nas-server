package com.easynas.server.service.impl;

import com.easynas.server.db.ConfigDb;
import com.easynas.server.db.UserDb;
import com.easynas.server.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author liangyongrui
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final ConfigDb configDb;

    @Autowired
    private UserDb userDb;

    @Autowired
    public AdminServiceImpl(ConfigDb configDb) {
        this.configDb = configDb;
    }

    @Override
    public String setGeneralInformationPath(String path) {
        File file = new File(path);
        if (file.exists()) {
            return "该路径已存在！";
        }
        String oldPath = configDb.getGeneralInformationPath();
        if (oldPath != null) {
            try {
                Process exec = Runtime.getRuntime().exec("mv " + oldPath + " " + path);
                exec.waitFor();
                configDb.setGeneralInformationPath(path);
            } catch (IOException | InterruptedException e) {
                log.error("setGeneralInformationPath，", e);
                return "移动历史文件失败" + e.toString();
            }
        } else {
            configDb.setGeneralInformationPath(path);
        }
        userDb.initAllUser();
        return null;
    }
}
