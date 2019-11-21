package com.github.smartbuf.springcloud.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserModel implements Serializable {
    private int    id;
    private String token;
    private String nickname;
    private String loginIp;
    private long   loginTime;
    private long   createTime;
    private long   updateTime;

    private List<UserModel> friends = new ArrayList<>();
}
