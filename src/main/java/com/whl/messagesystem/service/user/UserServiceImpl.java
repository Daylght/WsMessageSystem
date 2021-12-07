package com.whl.messagesystem.service.user;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import org.springframework.stereotype.Service;

/**
 * @author whl
 * @date 2021/12/7 18:48
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * 新用户注册
     *
     * @param userRegisterDto
     */
    @Override
    public Result register(UserRegisterDto userRegisterDto) {
        return null;
    }

    /**
     * 获取用户信息
     *
     * @param userName
     */
    @Override
    public Result getUserInfo(String userName) {
        return null;
    }

    /**
     * 更新用户信息
     *
     * @param userName
     */
    @Override
    public Result updateUserInfo(String userName) {
        return null;
    }

    /**
     * 删除用户
     *
     * @param userName
     */
    @Override
    public Result deleteUser(String userName) {
        return null;
    }
}
