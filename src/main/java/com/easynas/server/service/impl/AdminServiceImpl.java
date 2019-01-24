package com.easynas.server.service.impl;

import com.easynas.server.config.GlobalStatus;
import com.easynas.server.dao.UserDao;
import com.easynas.server.service.AdminService;
import com.easynas.server.service.ConfigService;
import com.easynas.server.service.FileService;
import com.easynas.server.util.CommandUtils;
import static com.easynas.server.util.CommandUtils.cp;
import static com.easynas.server.util.CommandUtils.rm;
import static com.easynas.server.util.FileUtils.scatterMove;
import static java.util.stream.Collectors.toList;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author liangyongrui
 */
@Slf4j
@Component
public class AdminServiceImpl implements AdminService {

    @Bean("adminService")
    @Autowired
    public AdminService getAdminService(
            @NonNull UserDao userDao,
            @Qualifier("configService") @NonNull ConfigService configService,
            @Qualifier("fileService") @NonNull FileService fileService) {
        return new AdminServiceImpl(userDao, configService, fileService);
    }

    @Bean("adminBackupService")
    @Autowired
    public AdminService getAdminBackupService(
            @NonNull UserDao userDao,
            @Qualifier("configBackupService") @NonNull ConfigService configService,
            @Qualifier("fileBackupService") @NonNull FileService fileService) {
        return new AdminServiceImpl(userDao, configService, fileService);
    }

    private final ConfigService configService;
    private final FileService fileService;
    private final UserDao userDao;

    public AdminServiceImpl(@NonNull UserDao userDao,
                            @NonNull ConfigService configService,
                            @NonNull FileService fileService) {
        this.configService = configService;
        this.fileService = fileService;
        this.userDao = userDao;
    }

    @Override
    public Optional<String> setGeneralInformationPath(@NonNull String path) {
        if (new File(path).exists()) {
            return Optional.of("该路径已存在！");
        }
        return setGeneralInformationPath(configService.getGeneralInformationPath(), path,
                configService::setGeneralInformationPath);
    }

    @Override
    public Optional<String> addFileSavePath(@NonNull String path) {
        return addFileSavePath(fileService.getFileSaveRootPaths(), path, fileService::setFileSavePath);
    }

    @Override
    public Optional<String> deleteFileSavePath(@NonNull String path) {
        return deleteFileSavePath(path, fileService.getFileSaveRootPaths(), fileService::setFileSavePath);
    }

    private Optional<String> addFileSavePath(@NonNull List<String> origin, @NonNull String adder,
                                             @NonNull Consumer<List<String>> fileSavePathConsumer) {
        try {
            String pathPartition = CommandUtils.getPathPartition(adder);
            for (String fileSavePath : origin) {
                String filePartition = CommandUtils.getPathPartition(fileSavePath);
                if (pathPartition.equals(filePartition)) {
                    return Optional.of(adder + " 所在分区，与" + fileSavePath + "所在分区相同，均为" + pathPartition);
                }
            }
        } catch (IOException e) {
            log.error("判断分区错误", e);
            return Optional.of("判断分区错误: " + e.toString());
        }
        origin.add(adder);
        fileSavePathConsumer.accept(origin);
        return Optional.empty();
    }

    private Optional<String> setGeneralInformationPath(@NonNull String source, @NonNull String destination,
                                                       @NonNull Consumer<String> destinationConsumer) {

        try {
            if (!GlobalStatus.setLock(true)) {
                log.error("加锁失败");
                throw new RuntimeException();
            }
            Process exec = cp(source, destination);
            exec.waitFor();
            destinationConsumer.accept(destination);
            GlobalStatus.setLock(false);
            rm(source);
        } catch (IOException | InterruptedException e) {
            final var errorMessage = "移动文件失败，source: " + source + ", destination: " + destination;
            log.error(errorMessage, e);
            return Optional.of(errorMessage);
        }
        userDao.initAllUser();
        return Optional.empty();
    }

    private Optional<String> deleteFileSavePath(@NonNull String path, @NonNull List<String> fileSavePaths,
                                                @NonNull Consumer<List<String>> setFileSavePathsConsumer) {
        final var toPaths = fileSavePaths.stream()
                .filter(s -> !path.equals(s)).collect(toList());
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
            setFileSavePathsConsumer.accept(toPaths);
            return Optional.empty();
        }
        final var toDirectories = toPaths.stream().map(File::new).collect(toList());
        if (scatterMove(fromDirectory, toDirectories)) {
            return Optional.of("删除失败，可能是剩余文件保存路径空间不足");
        }
        setFileSavePathsConsumer.accept(toPaths);
        return Optional.empty();
    }

}
