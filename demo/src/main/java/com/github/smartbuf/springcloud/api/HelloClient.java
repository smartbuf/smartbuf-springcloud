package com.github.smartbuf.springcloud.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "demo")
public interface HelloClient {

    @RequestMapping(value = "/hello", method = POST, consumes = "application/json", produces = "application/json")
    String helloJSON(@RequestParam("name") String name);

    @RequestMapping(value = "/hello", method = POST, consumes = "application/x-smartbuf", produces = "application/x-smartbuf")
    String helloSmartbuf(@RequestParam("name") String name);

}
