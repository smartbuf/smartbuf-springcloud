package com.github.smartbuf.springcloud;

import com.github.smartbuf.springcloud.api.HelloClient;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author sulin
 * @since 2019-11-21 10:27:29
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableDiscoveryClient
@EnableFeignClients
public class TestRunner {

    @Autowired
    HelloClient helloClient;

    @Test
    public void test() {
        System.out.println(helloClient.helloJSON("sulin"));
        System.out.println(helloClient.helloSmartbuf("sulin"));
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(1000);
    }
}
