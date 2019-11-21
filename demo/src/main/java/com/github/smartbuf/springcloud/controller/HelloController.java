package com.github.smartbuf.springcloud.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sulin
 * @since 2019-11-20 21:39:56
 */
@RestController
public class HelloController {

    @PostMapping(value = "/hello")
    public String sayHello(@RequestParam("name") String name) {
        return "hello " + name;
    }

}
