package com.easynas.server.service.impl;

import com.easynas.server.config.GlobalStatus;
import com.easynas.server.db.ConfigDb;
import com.easynas.server.db.UserDb;
import com.easynas.server.service.AdminService;
import com.easynas.server.util.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.easynas.server.util.FileUtils.canMove;

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
    public String setGeneralInformationPathBackup(String path) {
        File file = new File(path);
        if (file.exists()) {
            return "该路径已存在！";
        }
        String oldPath = configDb.getGeneralInformationPathBackup();
        if (oldPath != null) {
            boolean mvStatus = mv(oldPath, path, configDb::setGeneralInformationPathBackup);
            if (!mvStatus) {
                return "移动历史文件失败";
            }
        } else {
            configDb.setGeneralInformationPathBackup(path);
        }
        userDb.initAllUser();
        return null;
    }

    @Override
    public String addFileSavePath(String path) {
        List<String> fileSavePaths = configDb.getFileSavePaths();
        String checkMessage = checkPath(fileSavePaths, path);
        if (checkMessage != null) {
            return checkMessage;
        }
        fileSavePaths.add(path);
        configDb.setFileSavePath(fileSavePaths);
        return null;
    }

    @Override
    public String addFileSavePathBackup(String path) {
        List<String> fileSavePaths = configDb.getFileSavePathsBackup();
        String checkMessage = checkPath(fileSavePaths, path);
        if (checkMessage != null) {
            return checkMessage;
        }
        fileSavePaths.add(path);
        configDb.setFileSavePathBackup(fileSavePaths);
        return null;
    }

    @Override
    public String deleteFileSavePath(String path) {
        List<String> toPath = configDb.getFileSavePaths().stream()
                .filter(s -> !path.equals(s)).collect(Collectors.toList());
        if (toPath.size() == configDb.getFileSavePaths().size()) {
            return "删除失败，路径不存在";
        }
        File file = new File(path);
        if (file.isFile()) {
            return "删除失败，需要删除的路径内容为文件而不是一个文件夹";
        }
        if (Objects.requireNonNull(file.listFiles()).length == 0) {
            try {
                CommandUtils.rm(path);
            } catch (IOException e) {
                return "删除失败, " + e.toString();
            }
            configDb.setFileSavePath(toPath);
            return null;
        }
        if (!canMove(path, toPath)) {
            return "删除失败，剩余文件保存路径空间不足";
        }
        //todo 移动文件
        return null;
    }


    /**
     * 判断adder是否可以添加到路径中
     *
     * @param origin 现有的路径
     * @param adder  需要添加的路径
     * @return 可以返回null， 否则返回不能添加的原因
     */
    private String checkPath(List<String> origin, String adder) {
        try {
            String pathPartition = CommandUtils.getPathPartition(adder);
            for (String fileSavePath : origin) {
                String filePartition = CommandUtils.getPathPartition(fileSavePath);
                if (pathPartition.equals(filePartition)) {
                    return adder + " 所在分区，与" + fileSavePath + "所在分区相同，均为" + pathPartition;
                }
            }
        } catch (IOException e) {
            log.error("判断分区错误", e);
            return "判断分区错误: " + e.toString();
        }
        return null;
    }

    private boolean mv(String source, String destination, Consumer<String> destinationConsumer) {
        try {
            if (!GlobalStatus.setLock(true)) {
                log.error("加锁失败");
                return false;
            }
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
