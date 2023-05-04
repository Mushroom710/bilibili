package com.bilibili.service;

import com.bilibili.dao.UserFollowingDao;
import com.bilibili.domain.FollowingGroup;
import com.bilibili.domain.User;
import com.bilibili.domain.UserFollowing;
import com.bilibili.domain.UserInfo;
import com.bilibili.domain.constant.UserConstant;
import com.bilibili.domain.exception.ConditionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// @date 2023/4/23
// @time 21:12
// @author zhangzhi
// @description
@Service
public class UserFollowingService {

    @Resource
    private UserFollowingDao userFollowingDao;

    @Resource
    private FollowingGroupService followingGroupService;

    @Resource
    private UserService userService;

    // 新增用户关注
    @Transactional
    public void addUserFollowings(UserFollowing userFollowing){
        Long groupId = userFollowing.getGroupId();
        // 没有指定分组，就添加到默认分组
        if(groupId == null){
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        }else{
            FollowingGroup followingGroup = followingGroupService.getById(groupId);
            if(followingGroup == null){
                throw new ConditionException("关注的分组不存在！");
            }
        }
        // 取出要关注的用户 id
        Long followingId = userFollowing.getFollowingId();
        // 查询这个用户
        User user = userService.getUserById(followingId);
        if(user == null){
            throw new ConditionException("要关注的用户不存在！");
        }
        // 删除之前的关注记录
        userFollowingDao.deleteUserFollowing(userFollowing.getUserId(), followingId);
        // 新增一条关注记录
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);
    }


    // 1.获取关注的用户列表
    // 2.根据关注用户的id查询关注用户的基本信息
    // 3.将关注用户按关注分组进行分类
    public List<FollowingGroup> getUserFollowings(Long userId){
        // 获取当前用户的关注列表
        List<UserFollowing> list = userFollowingDao.getUserFollowings(userId);
        // 取出关注列表的用户 id
        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if(followingIdSet.size() > 0){
            // 这里为什么还要再查用户信息？因为用户关注只是添加了一个 followingId，没有对应的信息，
            // 所以还需要另外根据 followingId 查询一遍用户的信息
            // 根据关注列表的用户 id 批量查询用户信息
            userInfoList =  userService.getUserInfoByUserIds(followingIdSet);
        }
        // 把批量查询出来的用户信息填充到对应的 userFollowing
        for(UserFollowing userFollowing : list){
            for(UserInfo userInfo : userInfoList){
                // 被关注的用户 id == 查询出来的用户 id
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    // 填充被关注的用户信息
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        // 查询出这个用户的所有分组信息
        // 包括用户自己创建的和系统默认提供的分组信息
        List<FollowingGroup> groupList = followingGroupService.getByUserId(userId);
        FollowingGroup allGroup = new FollowingGroup();
        // 全部关注分组，这个分组就是全部的关注信息
        allGroup.setName(UserConstant.USER_FOLLOWINGS_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);
        // 构建用户的分组构建对应的关注列表
        List<FollowingGroup> result =new ArrayList<>();
        // 全部关注分组
        result.add(allGroup);
        // 用户自定义分组和系统默认分组
        for(FollowingGroup group : groupList){
            List<UserInfo> infoList = new ArrayList<>();
            // 根据关注用户的分组信息填充到对应的分组
            for(UserFollowing userFollowing : list){
                // 分组 id == 被关注用户的分组 id
                if(group.getId().equals(userFollowing.getGroupId())){
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoList(infoList);
            result.add(group);
        }
        // result 中就构建好了分组信息对应的关注用户信息
        return result;
    }

    // 这个方法的实现和获取关注列表基本一致
    // 1.获取当前用户的粉丝列表
    // 2.根据粉丝的用户 id 查询基本信息
    // 3.查询当前用户是否已经关注该粉丝 -- 互关
    public List<UserFollowing> getUserFans(Long userId){
        // 根据用户 id 查询关注了这个用户的用户关注信息
        List<UserFollowing> fanList =  userFollowingDao.getUserFans(userId);
        // 从 fanList 中取出用户的 id
        Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if(fanIdSet.size() > 0){
            // 根据 fanIdSet 查询对应的用户信息
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }
        // 这里是用于处理两个用户是不是互粉状态
        // 根据 userId 查询出这个用户的关注列表
        List<UserFollowing> followingList = userFollowingDao.getUserFollowings(userId);
        // 如果这个用户的粉丝关注了他，他也关注了这个粉丝，那么就处于互粉的状态
        for(UserFollowing fan : fanList){
            for(UserInfo userInfo : userInfoList){
                // 填充粉丝信息
                if(fan.getUserId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(false);
                    fan.setUserInfo(userInfo);
                }
            }
            // 如果 userId 的关注列表里有粉丝的 id，说明两个人是互粉状态
            // 设置 followed 为 true
            for(UserFollowing following : followingList){
                if(following.getFollowingId().equals(fan.getUserId())){
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }
        // 返回粉丝列表
        return fanList;
    }

    // 新增用户自定义的关注分组
    public Long addUserFollowingGroups(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWINGS_GROUP_TYPE_USER);
        followingGroupService.addFollowingGroup(followingGroup);
        return followingGroup.getId();
    }

    // 查询出用户的所有自定义的关注分组信息
    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupService.getUserFollowingGroups(userId);
    }

    // 检查是不是互粉状态
    public List<UserInfo> checkFollowingStatus(List<UserInfo> userInfoList, Long userId) {
        // 获取用户的关注列表
        List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowings(userId);
        for(UserInfo userInfo : userInfoList){
            userInfo.setFollowed(false);
            for(UserFollowing userFollowing : userFollowingList){
                // 用户的关注列表里有粉丝，粉丝也关注了用户，说明是互粉状态
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(true);
                }
            }
        }
        return userInfoList;
    }
}
