package com.whl.messagesystem.service.user;

import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author whl
 * @date 2021/12/7 18:48
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    /**
     * 新用户注册
     * @param userRegisterDto
     * 用户注册所需信息
     */
    @Override
    public Result register(UserRegisterDto userRegisterDto) {
        try {
            if (userRegisterDto == null) {
                throw new NullPointerException("参数为空");
            }
            //todo:实现dao层
        } catch (Exception e) {
            log.error("注册失败: {}", e.getMessage());
            return ResultUtil.error();
        }
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
