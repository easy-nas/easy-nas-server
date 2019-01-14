package com.easynas.server.controller;

import com.easynas.server.model.Result;
import com.easynas.server.model.admin.request.PathRequest;
import com.easynas.server.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(value = "设置通用信息保存路径", notes = "失败返回错误")
    @PostMapping("set-general-information-path")
    public Result<String> setGeneralInformationPath(@RequestBody PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminService::setGeneralInformationPath);
    }

    @ApiOperation(value = "设置通用信息备份路径", notes = "失败返回错误")
    @PostMapping("set-general-information-path-backup")
    public Result<String> setGeneralInformationPathBackup(@RequestBody PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminService::setGeneralInformationPathBackup);
    }

    @ApiOperation(value = "添加文件保存路径", notes = "失败返回错误")
    @PostMapping("add-file-save-path")
    public Result<String> addFileSavePath(@RequestBody PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminService::addFileSavePath);
    }

    @ApiOperation(value = "添加文件备份路径", notes = "失败返回错误")
    @PostMapping("add-file-save-path-backup")
    public Result<String> addFileSavePathBackup(@RequestBody PathRequest pathRequest) {
        return dealPath(pathRequest.getPath(), adminService::addFileSavePathBackup);
    }

    @ApiOperation(value = "WIP:删除文件保存路径", notes = "失败返回错误")
    @PostMapping("delete-file-save-path")
    public Result<String> deleteFileSavePath(@RequestBody PathRequest pathRequest) {
        return Result.success();
    }

    @ApiOperation(value = "WIP:删除文件备份路径", notes = "失败返回错误")
    @PostMapping("delete-file-save-path")
    public Result<String> deleteFileSavePathBackup(@RequestBody PathRequest pathRequest) {
        return Result.success();
    }


    private Result<String> dealPath(String path, Function<String, String> function) {
        if (path == null || path.strip().length() == 0) {
            return Result.fail("路径为空");
        }
        String errorMessage = function.apply(path);
        if (errorMessage == null) {
            return Result.success();
        }
        return Result.fail(errorMessage);
    }

}
