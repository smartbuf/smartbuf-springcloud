package com.github.smartbuf.springcloud;

import com.github.smartbuf.springcloud.api.DemoClient;
import com.github.smartbuf.springcloud.model.PostModel;
import com.github.smartbuf.springcloud.model.UserModel;
import com.github.smartbuf.springcloud.utils.NetMonitor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sulin
 * @since 2019-11-21 10:27:29
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableDiscoveryClient
@EnableFeignClients
public class DemoTest {

    private static int K = 1000;

    @Autowired
    private DemoClient demoClient;

    @Test
    public void test() {
        String hello1 = demoClient.helloJSON("sulin");
        String hello2 = demoClient.helloSmartbuf("sulin");
        assert hello1.equals(hello2);

        UserModel user1 = demoClient.getUserJSON(10001);
        UserModel user2 = demoClient.getUserSmartbuf(10001);
        assert user1.equals(user2);

        List<PostModel> posts1 = demoClient.queryPostJSON("hollywood");
        List<PostModel> posts2 = demoClient.queryPostSmartbuf("hollywood");
        assert Arrays.equals(posts1.toArray(), posts2.toArray());
    }

    @Test
    public void helloJson() {
        runConsumer(400 * K, () -> demoClient.helloJSON("sulin"));
    }

    @Test
    public void helloSmartbuf() {
        runConsumer(400 * K, () -> demoClient.helloSmartbuf("sulin"));
    }

    @Test
    public void getUserJson() {
        runConsumer(300 * K, () -> demoClient.getUserJSON(10001));
    }

    @Test
    public void getUserSmartbuf() {
        runConsumer(300 * K, () -> demoClient.getUserSmartbuf(10001));
    }

    @Test
    public void queryPostJson() {
        runConsumer(100 * K, () -> demoClient.queryPostJSON("hollywood"));
    }

    @Test
    public void queryPostSmartbuf() {
        runConsumer(100 * K, () -> demoClient.queryPostSmartbuf("hollywood"));
    }

    private void runConsumer(int times, Runnable run) {
        StringBuffer buffer = new StringBuffer();
        AtomicInteger counter = new AtomicInteger();
        AtomicInteger errorTimes = new AtomicInteger();

        NetMonitor netMonitor = new NetMonitor();
        netMonitor.start();
        new Thread(() -> {
            for (int i = 0; i < times; i++) {
                try {
                    run.run();
                } catch (Exception e) {
                    log.error("wtf! ", e);
                    errorTimes.incrementAndGet();
                }
                counter.incrementAndGet();
            }
        }).start();

        int second = 0;
        int t;
        do {
            sleep(1);
            long bytesIn = netMonitor.getBytesIn();
            long bytesOut = netMonitor.getBytesOut();
            t = counter.get();
            buffer.append(second++).append(", ")
                .append(t).append(", ")
                .append(bytesIn + bytesOut).append(", ")
                .append(bytesIn).append(", ")
                .append(bytesOut).append('\n');
            System.out.printf("%d, %d, %d, %d, %d\n", second, t, bytesIn + bytesOut, bytesIn, bytesOut);
        } while (t < times);

        netMonitor.stop();

        System.out.println("error times: " + errorTimes.get());
        System.out.println("+++++++++++++++++++++");
        System.out.println(buffer);
        System.out.println("+++++++++++++++++++++");

        sleep(3);
    }

    static void sleep(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException ignored) {
        }
    }

}
