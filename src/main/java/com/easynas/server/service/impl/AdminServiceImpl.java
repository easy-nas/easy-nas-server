package com.easynas.server.service.impl;

import com.easynas.server.config.GlobalStatus;
import com.easynas.server.dao.UserDao;
import com.easynas.server.handler.RuntimeHandler;
import com.easynas.server.service.AdminService;
import com.easynas.server.service.ConfigService;
import com.easynas.server.service.FileService;
import com.easynas.server.util.CommandUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.easynas.server.util.CollectionUtils.addList;
import static com.easynas.server.util.CommandUtils.cp;
import static com.easynas.server.util.CommandUtils.rm;
import static com.easynas.server.util.FileUtils.copyFiles;
import static com.easynas.server.util.FileUtils.getMoveMap;
import static java.util.stream.Collectors.toList;

/**
 * @author liangyongrui
 */
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final ConfigService configService;
    private final FileService fileService;
    private final UserDao userDao;
    private final RuntimeHandler runtimeHandler;
    /**
     * 当前的service的类型 ['master','backup']
     */
    private final String type;

    public AdminServiceImpl(@NonNull final UserDao userDao,
                            @NonNull final RuntimeHandler runtimeHandler,
                            @NonNull final ConfigService configService,
                            @NonNull final FileService fileService,
                            @NonNull final String type) {
        this.configService = configService;
        this.fileService = fileService;
        this.userDao = userDao;
        this.runtimeHandler = runtimeHandler;
        this.type = type;
    }

    @Override
    public Optional<String> setGeneralInformationPath(@NonNull final String path) {
        if (new File(path).exists()) {
            return Optional.of("该路径已存在！");
        }
        final var source = configService.getGeneralInformationPath();
        try {
            if (!GlobalStatus.setLock(true)) {
                log.error("加锁失败");
                throw new RuntimeException();
            }
            final var exec = cp(source, path);
            exec.waitFor();
            configService.setGeneralInformationPath(path);
            GlobalStatus.setLock(false);
            rm(source);
        } catch (IOException | InterruptedException e) {
            final var errorMessage = "移动文件失败，source: " + source + ", destination: " + path;
            log.error(errorMessage, e);
            return Optional.of(errorMessage);
        }
        userDao.initAllUser();
        return Optional.empty();
    }

    @Override
    public Optional<String> addFileSavePath(@NonNull final String path) {
        final var origin = fileService.getFileSaveRootPaths();
        final var adderFile = new File(path);
        if (!adderFile.exists() && !adderFile.mkdirs()) {
            return Optional.of("添加路径" + path + "失败");
        }
        final var checkResult = runtimeHandler.supplyInProduction((Supplier<Optional<String>>) () -> {
            try {
                final var pathPartition = CommandUtils.getPathPartition(path);
                for (String fileSavePath : origin) {
                    String filePartition = CommandUtils.getPathPartition(fileSavePath);
                    if (pathPartition.equals(filePartition)) {
                        return Optional.of(path + " 所在分区，与" + fileSavePath + "所在分区相同，均为" + pathPartition);
                    }
                }
            } catch (IOException e) {
                log.error("判断分区错误", e);
                return Optional.of("判断分区错误: " + e.toString());
            }
            return Optional.empty();
        }, Optional.empty());
        if (checkResult.isPresent()) {
            return checkResult;
        }
        fileService.setFileSavePath(addList(origin, path));
        return Optional.empty();
    }

    @Override
    public Optional<String> deleteFileSavePath(@NonNull final String path) {
        final var fileSavePaths = fileService.getFileSaveRootPaths();
        final var toPaths = fileSavePaths.stream().filter(s -> !path.equals(s)).collect(toList());
        if (toPaths.size() == fileSavePaths.size()) {
            return Optional.of("删除失败，路径不存在");
        }
        final var fromDirectory = new File(path);
        if (fromDirectory.isFile()) {
            return Optional.of("删除失败，需要删除的路径内容为文件而不是一个文件夹");
        }
        final var files = fromDirectory.listFiles();
        assert files != null;
        if (files.length == 0) {
            try {
                rm(path);
            } catch (IOException e) {
                return Optional.of("删除失败, " + e.toString());
            }
            fileService.setFileSavePath(toPaths);
            return Optional.empty();
        }
        final var toDirectories = toPaths.stream().map(File::new).collect(toList());
        final var moveMap = getMoveMap(fromDirectory, toDirectories);
        if (moveMap.isEmpty()) {
            return Optional.of("删除失败，可能是剩余文件保存路径空间不足");
        }
        log.info("源文件地址 => 目的文件地址：" + moveMap);
        final var moveResult = moveFiles(moveMap);
        fileService.setFileSavePath(toPaths);
        return Optional.empty();
    }

    /**
     * 移动文件，并改变用户的软连接
     *
     * @param moveMap
     * @return
     */
    private boolean moveFiles(Map<String, String> moveMap) {
        if (copyFiles(moveMap)) {
            log.error("moveFiles, 复制文件失败");
            return false;
        }
        //todo
        return true;
    }

}
