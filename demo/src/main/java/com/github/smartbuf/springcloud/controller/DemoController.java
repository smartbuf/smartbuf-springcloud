package com.github.smartbuf.springcloud.controller;

import com.github.smartbuf.springcloud.model.PostModel;
import com.github.smartbuf.springcloud.model.TopicModel;
import com.github.smartbuf.springcloud.model.UserModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sulin
 * @since 2019-11-20 21:39:56
 */
@RestController
public class DemoController {

    private UserModel       user;
    private List<PostModel> posts = new ArrayList<>();

    public DemoController() {
        for (int i = 0; i < 20; i++) {
            UserModel user = new UserModel();
            user.setId(RandomUtils.nextInt(10000, 99999));
            user.setNickname(RandomStringUtils.randomAlphanumeric(10));
            user.setLoginIp("127.0.0.1");
            user.setLoginTime(10);
            user.setCreateTime(System.currentTimeMillis());
            user.setUpdateTime(System.currentTimeMillis());
            user.setToken(UUID.randomUUID().toString());
            if (i == 0) {
                this.user = user;
            } else {
                this.user.getFriends().add(user);
            }
        }

        for (int i = 0; i < 100; i++) {
            PostModel post = PostModel.random();
            for (int j = 0; j < 5; j++) {
                post.getTopics().add(TopicModel.getFromPool());
            }
            posts.add(post);
        }
    }

    @PostMapping("/hello")
    public String sayHello(String name) { return "hello " + name; }

    @PostMapping("/getUser")
    public UserModel getUser(Integer userId) { return user; }

    @PostMapping("/queryPost")
    public List<PostModel> queryPost(String keyword) { return posts; }

}
