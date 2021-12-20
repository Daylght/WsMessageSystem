package com.whl.messagesystem.service.user;

import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.dao.UserDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import com.whl.messagesystem.model.dto.UserUpdateDto;
import com.whl.messagesystem.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.ValidationException;
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
     *
     * @param userRegisterDto 用户注册所需信息
     */
    @Override
    public Result register(UserRegisterDto userRegisterDto) {
        try {
            if (userRegisterDto == null) {
                throw new ValidationException("参数为空");
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
     * 更新用户信息
     */
    @Override
    public Result updateUserInfo(UserUpdateDto userUpdateDto) {
        try {
            if (userUpdateDto == null) {
                throw new ValidationException("参数为空");
            }

            log.info("更新的用户信息: {}", userUpdateDto);
            User user = new User(userUpdateDto);
            if (userDao.updateAnUser(user)) {
                return ResultUtil.success();
            }

            throw new SQLException("更新用户记录失败");
        } catch (Exception e) {
            log.error("更新用户信息失败: {}", e.getMessage());
            return ResultUtil.error();
        }
    }

    /**
     * 删除用户
     *
     * @param userName 用户名
     */
    @Override
    public Result deleteUser(String userName) {
        try {
            if (userName == null) {
                throw new ValidationException("参数为空");
            }

            log.info("删除的用户名为: {}", userName);
            if (userDao.logicalDeleteAnUser(userName)) {
                return ResultUtil.success();
            }

            throw new SQLException("逻辑删除用户失败");
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage());
            return ResultUtil.error();
        }
    }
}
