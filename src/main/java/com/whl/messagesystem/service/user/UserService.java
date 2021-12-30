package com.whl.messagesystem.service.user;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import com.whl.messagesystem.model.dto.UserUpdateDto;

import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/7 18:47
 */
public interface UserService {
    /**
     * 新用户注册
     */
    Result register(String userName, String password);

    /**
     * 更新用户名、密码
     */
    Result updateUserNameAndPassword(String userId, String userName, String password, HttpSession session);

    /**
     * 删除用户
     */
    Result deleteUser(String userId);

}
