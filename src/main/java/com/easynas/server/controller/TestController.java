package com.easynas.server.controller;

import com.easynas.server.model.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangyongrui
 * @date 2019-01-09 22:51:43
 */
@RestController
@RequestMapping("api")
@Slf4j
public class TestController {

    @PostMapping("test-request")
    public Result<TestRequest> todo(@RequestBody @NonNull final TestRequest testRequest) {
        return Result.success(testRequest);
    }
}

@ToString
@AllArgsConstructor
@Getter
class TestRequest {
    private final String abc;
    private final String def;
}
