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

/**
 * @author liangyongrui
 */
@RestController
@RequestMapping("api/admin")
@Slf4j
@Api("管理员操作相关api")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "设置通用信息保存路径", notes = "失败返回错误")
    @PostMapping("set-general-information-path")
    public Result<String> setGeneralInformationPath(@RequestBody PathRequest pathRequest) {
        String s = adminService.setGeneralInformationPath(pathRequest.getPath());
        if (s == null) {
            return Result.success();
        }
        return Result.fail(s);
    }
}
