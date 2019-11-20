package com.github.smartbuf.springcloud.api;

import com.github.smartbuf.springcloud.SmartbufFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "TEST", configuration = SmartbufFeignConfiguration.class)
public interface HelloClient {

    @RequestMapping(value = "/hello", method = POST, consumes = "application/x-protobuf", produces = "application/x-protobuf")
    String sayHello(@RequestParam("name") String name);

}
