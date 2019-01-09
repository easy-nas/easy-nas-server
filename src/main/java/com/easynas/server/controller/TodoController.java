package com.easynas.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-1-9 下午6:26
 */
@RestController
@RequestMapping("")
@Slf4j
public class TodoController {

    @GetMapping("todo")
    public String todo() {
        return "todo22asdf2";
    }
}
