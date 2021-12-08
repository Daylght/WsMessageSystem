package com.whl.messagesystem.service.user;

import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.dao.UserDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import com.whl.messagesystem.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * @author whl
 * @date 2021/12/7 18:48
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserDao userDao;

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

            User user = new User(userRegisterDto);
            log.info("新用户信息: {}", user);
            if (userDao.insertAnUser(user)) {
                return ResultUtil.success();
            }

            throw new SQLException("插入新用户失败");
        } catch (Exception e) {
            log.error("注册失败: {}", e.getMessage());
            return ResultUtil.error();
        }
    }

    /**
     * 获取用户信息
     * @param userName
     * 用户名
     */
    @Override
    public Result getUserInfo(String userName) {
        return null;
    }

    /**
     * 更新用户信息
     * @param userName
     * 用户名
     */
    @Override
    public Result updateUserInfo(String userName) {
        return null;
    }

    /**
     * 删除用户
     * @param userName
     * 用户名
     */
    @Override
    public Result deleteUser(String userName) {
        return null;
    }
}
