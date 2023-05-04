package com.bilibili.domain.auth;

import lombok.Data;

import java.util.Date;

// @date 2023/5/2
// @time 16:47
// @author zhangzhi
// @description
@Data
public class AuthElementOperation {
    private Long id;

    private String elementName;

    private String elementCode;

    private String operationType;

    private Date createTime;

    private Date updateTime;
}
