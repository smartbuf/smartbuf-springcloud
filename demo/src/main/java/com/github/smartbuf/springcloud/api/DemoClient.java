package com.github.smartbuf.springcloud.api;

import com.github.smartbuf.springcloud.model.PostModel;
import com.github.smartbuf.springcloud.model.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "demo")
public interface DemoClient {

    @PostMapping(value = "/hello", consumes = "application/json", produces = "application/json")
    String helloJSON(@RequestParam("name") String name);

    @PostMapping(value = "/hello", consumes = "application/x-smartbuf", produces = "application/x-smartbuf")
    String helloSmartbuf(@RequestParam("name") String name);

    @PostMapping(value = "/getUser", consumes = "application/json", produces = "application/json")
    UserModel getUserJSON(@RequestParam("userId") Integer userId);

    @PostMapping(value = "/getUser", consumes = "application/x-smartbuf", produces = "application/x-smartbuf")
    UserModel getUserSmartbuf(@RequestParam("userId") Integer userId);

    @PostMapping(value = "/queryPost", consumes = "application/json", produces = "application/json")
    List<PostModel> queryPostJSON(@RequestParam("keyword") String keyword);

    @PostMapping(value = "/queryPost", consumes = "application/x-smartbuf", produces = "application/x-smartbuf")
    List<PostModel> queryPostSmartbuf(@RequestParam("keyword") String keyword);

}
