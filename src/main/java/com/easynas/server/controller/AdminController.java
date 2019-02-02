package com.easynas.server.controller;

import com.easynas.server.model.Result;
import com.easynas.server.model.admin.request.PathRequest;
import com.easynas.server.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author liangyongrui
 */
@RestController
@RequestMapping("api/admin")
@Slf4j
@Api("管理员操作相关api")
public class AdminController {

    private final AdminService adminService;
    private final AdminService adminBackupService;

    @Autowired
    public AdminController(@Qualifier("adminService") @NonNull final AdminService adminService,
                           @Qualifier("adminBackupService") @NonNull final AdminService adminBackupService) {
        this.adminService = adminService;
        this.adminBackupService = adminBackupService;
    }

    @ApiOperation(value = "设置通用信息保存路径", notes = "失败返回错误")
    @PostMapping("set-general-information-path")
    public Result<String> setGeneralInformationPath(@RequestBody @NonNull final PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminService::setGeneralInformationPath);
    }

    @ApiOperation(value = "设置通用信息备份路径", notes = "失败返回错误")
    @PostMapping("set-general-information-path-backup")
    public Result<String> setGeneralInformationPathBackup(@RequestBody @NonNull final PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminBackupService::setGeneralInformationPath);
    }

    @ApiOperation(value = "添加文件保存路径", notes = "失败返回错误")
    @PostMapping("add-file-save-path")
    public Result<String> addFileSavePath(@RequestBody @NonNull final PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminService::addFileSavePath);
    }

    @ApiOperation(value = "添加文件备份路径", notes = "失败返回错误")
    @PostMapping("add-file-save-path-backup")
    public Result<String> addFileSavePathBackup(@RequestBody @NonNull final PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminBackupService::addFileSavePath);
    }

    @ApiOperation(value = "删除文件保存路径", notes = "失败返回错误")
    @PostMapping("delete-file-save-path")
    public Result<String> deleteFileSavePath(@RequestBody @NonNull final PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminService::deleteFileSavePath);
    }

    @ApiOperation(value = "删除文件备份路径", notes = "失败返回错误")
    @PostMapping("delete-file-save-path-backup")
    public Result<String> deleteFileSavePathBackup(@RequestBody @NonNull final PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminBackupService::deleteFileSavePath);
    }

    @ApiOperation(value = "WIP:交换文件存储路径和备份路径", notes = "失败返回错误")
    @PostMapping("swap-file-path-and-backup-path")
    public Result<String> swapFilePathAndBackupPath() {
        //todo
        return Result.success();
    }

    @ApiOperation(value = "WIP:交换通用信息保存路径和备份路径", notes = "失败返回错误")
    @PostMapping("swap-general-information-path-and-backup-path")
    public Result<String> swapGeneralInformationPathAndBackupPath() {
        //todo
        return Result.success();
    }

    private Result<String> dealPath(@NonNull final String path, @NonNull final Function<String, Optional<String>> function) {
        if (path == null || path.strip().length() == 0) {
            return Result.fail("路径为空");
        }
        final var errorMessage = function.apply(path);
        if (errorMessage.isEmpty()) {
            return Result.success();
        }
        return Result.fail(errorMessage.get());
    }

}
