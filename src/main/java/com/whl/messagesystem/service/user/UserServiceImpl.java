package com.whl.messagesystem.service.user;

import com.whl.messagesystem.commons.constant.UserConstant;
import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.dao.UserDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import com.whl.messagesystem.model.dto.UserUpdateDto;
import com.whl.messagesystem.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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
     */
    @Override
    public Result register(String userName, String password) {
        try {
            if (StringUtils.isAnyBlank(userName, password)) {
                throw new ValidationException("参数为空");
            }

            User user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            log.info("要插入的用户信息为: {}", user);
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
     * 更新用户名、密码
     */
    @Override
    public Result updateUserNameAndPassword(String userId, String userName, String password, HttpSession session) {
        try {
            if (StringUtils.isAnyBlank(userId, userName, password)) {
                throw new ValidationException("参数为空");
            }

            User user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setPassword(password);
            log.info("更新的用户信息: {}", user);
            if (userDao.updateUserNameAndPassword(user)) {
                // 更新会话
                session.setAttribute(UserConstant.USER, user);
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
     * @param userId 用户id
     */
    @Override
    public Result deleteUser(String userId) {
        try {
            if (StringUtils.isAnyBlank(userId)) {
                throw new ValidationException("参数为空");
            }

            log.info("删除的用户id为: {}", userId);
            if (userDao.logicalDeleteAnUser(userId)) {
                return ResultUtil.success();
            }

            throw new SQLException("逻辑删除用户失败");
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage());
            return ResultUtil.error();
        }
    }


}
