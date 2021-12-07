package com.whl.messagesystem.service.user;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;

/**
 * @author whl
 * @date 2021/12/7 18:47
 */
@SuppressWarnings("all")
public interface UserService {
    /**
     * 新用户注册
     */
    Result register(UserRegisterDto userRegisterDto);

    /**
     * 获取用户信息
     */
    Result getUserInfo(String userName);

    /**
     * 更新用户信息
     */
    Result updateUserInfo(String userName);

    /**
     * 删除用户
     */
    Result deleteUser(String userName);
}
