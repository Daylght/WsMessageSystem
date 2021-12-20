package com.whl.messagesystem.service.user;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import com.whl.messagesystem.model.dto.UserUpdateDto;
import com.whl.messagesystem.model.entity.User;

import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/7 18:47
 */
public interface UserService {
    /**
     * 新用户注册
     */
    Result register(UserRegisterDto userRegisterDto);

    /**
     * 更新用户信息
     */
    Result updateUserInfo(UserUpdateDto userUpdateDto);

    /**
     * 删除用户
     */
    Result deleteUser(String userName);
}
