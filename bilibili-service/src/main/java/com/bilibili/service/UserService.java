package com.bilibili.service;

// @date 2023/4/22
// @time 20:11
// @author zhangzhi
// @description

import com.alibaba.fastjson.JSONObject;
import com.bilibili.dao.UserDao;
import com.bilibili.domain.PageResult;
import com.bilibili.domain.RefreshTokenDetail;
import com.bilibili.domain.User;
import com.bilibili.domain.UserInfo;
import com.bilibili.domain.constant.UserConstant;
import com.bilibili.domain.exception.ConditionException;
import com.bilibili.service.util.MD5Util;
import com.bilibili.service.util.RSAUtil;
import com.bilibili.service.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private UserAuthService userAuthService;

    @Transactional
    public void addUser(User user) {
        String phone = user.getPhone();
        // 检查用户手机号
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空！");
        }
        // 查询数据库，检查这个手机号有没有注册过
        User dbUser = this.getUserByPhone(phone);
        if(dbUser != null){
            throw new ConditionException("该手机号已经注册！");
        }
        // 生成 salt
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String password = user.getPassword();
        String rawPassword = "";
        // 解密前端传过来的用户账号密码
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }
        // 创建用户的账号信息
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userDao.addUser(user);

        // 创建用户详细信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);
        userInfo.setUpdateTime(now);
        userDao.addUserInfo(userInfo);

        // 添加用户默认权限角色
        userAuthService.addUserDefaultRole(user.getId());
    }

    // 根据手机号查询用户账号信息
    public User getUserByPhone(String phone){
        return userDao.getUserByPhone(phone);
    }

    // 用户登录
    public String login(User user) throws Exception {
        String phone = user.getPhone();
        // 判断手机号
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空！");
        }
        User dbUser = this.getUserByPhone(phone);
        // 用户是否注册
        if(dbUser == null){
            throw new ConditionException("该用户未注册！");
        }
        String password = user.getPassword();
        String rawPassword = "";
        // 拿到用户登录密码
        try{
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("密码解密失败！");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        // 比较密码和数据库密码是否一样
        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误！");
        }

        // 返回登录令牌
        return TokenUtil.generateToken(dbUser.getId());
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUsers(User user) throws Exception{
        Long id = user.getId();
        User dbUser = userDao.getUserById(id);
        if(dbUser == null){
            throw new ConditionException("用户不存在！");
        }
        if(!StringUtils.isNullOrEmpty(user.getPassword())){
            String rawPassword = RSAUtil.decrypt(user.getPassword());
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }
        user.setUpdateTime(new Date());
        userDao.updateUsers(user);
    }

    public void updateUserInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        userDao.updateUserInfos(userInfo);
    }

    // 查询用户信息
    public User getUserById(Long followingId) {
        return userDao.getUserById(followingId);
    }

    // 通过 id 批量查询用户详细信息
    public List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList) {
        return userDao.getUserInfoByUserIds(userIdList);
    }

    // 分页查询用户信息
    public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        params.put("start", (no - 1) * size);
        params.put("limit", size);
        // 计算 total
        Integer total = userDao.pageCountUserInfos(params);
        List<UserInfo> list = new ArrayList<>();
        if(total > 0){
            // 查询用户信息
            list = userDao.pageListUserInfos(params);
        }
        return new PageResult<>(total, list);
    }

    // 双令牌实现登录升级
    @Transactional
    public Map<String, Object> loginForDts(User user) throws Exception {
        String phone = user.getPhone();
        // 判断手机号
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空！");
        }
        User dbUser = this.getUserByPhone(phone);
        // 用户是否注册
        if(dbUser == null){
            throw new ConditionException("该用户未注册！");
        }
        String password = user.getPassword();
        String rawPassword = "";
        // 拿到用户登录密码
        try{
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("密码解密失败！");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        // 比较密码和数据库密码是否一样
        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误！");
        }

        Long userId = dbUser.getId();
        // 返回登录令牌
        // 接入 token
        String accessToken = TokenUtil.generateToken(userId);
        // 刷新 token
        String refreshToken = TokenUtil.generateRefreshToken(userId);
        // 保存 refreshToken 到数据库
        userDao.deleteRefreshToken(refreshToken, userId);
        userDao.addRefreshToken(refreshToken, userId, new Date());

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return result;
    }

    public void logout(String refreshToken, Long userId) {
        userDao.deleteRefreshToken(refreshToken, userId);
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        RefreshTokenDetail refreshTokenDetail = userDao.getRefreshTokenDetail(refreshToken);
        if(refreshTokenDetail == null){
            throw new ConditionException("555", "token过期！");
        }
        Long userId = refreshTokenDetail.getUserId();
        return TokenUtil.generateToken(userId);
    }
}
