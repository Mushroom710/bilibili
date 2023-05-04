package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/5/3
// @time 10:16
// @author zhangzhi
// @description
@Data
public class RefreshTokenDetail {

    private Long id;

    private String refreshToken;

    private Long userId;

    private Date creatTime;
}
