package com.easynas.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangyongrui
 * @date 2019-01-09 22:51:43
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
