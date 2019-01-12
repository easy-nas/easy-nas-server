package com.easynas.server.controller;


import com.easynas.server.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangyongrui
 */
@RestController
@RequestMapping("api/upload")
@Slf4j
public class UploadController {

    public Result uploadFlie() {
        return Result.success();
    }

    public Result uploadDirectory() {
        return Result.success();
    }
}
