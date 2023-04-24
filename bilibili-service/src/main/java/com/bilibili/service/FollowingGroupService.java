package com.bilibili.service;

import com.bilibili.dao.FollowingGroupDao;
import com.bilibili.domain.FollowingGroup;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

// @date 2023/4/23
// @time 21:13
// @author zhangzhi
// @description
@Service
public class FollowingGroupService {

    @Resource
    private FollowingGroupDao followingGroupDao;

    // 根据 type 查询分组信息
    public FollowingGroup getByType(String type){
        return followingGroupDao.getByType(type);
    }

    // 根据 id 查分组信息
    public FollowingGroup getById(Long id){
        return followingGroupDao.getById(id);
    }

    // 根据用户 id 查询用户对应的所有分组信息包括系统提供的默认分组信息
    public List<FollowingGroup> getByUserId(Long userId) {
        return followingGroupDao.getByUserId(userId);
    }

    // 新增用户自定义分组
    public void addFollowingGroup(FollowingGroup followingGroup) {
        followingGroupDao.addFollowingGroup(followingGroup);
    }

    // 根据用户 id 查询用户对应的所有分组信息
    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupDao.getUserFollowingGroups(userId);
    }
}
