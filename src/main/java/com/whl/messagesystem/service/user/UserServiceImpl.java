package com.whl.messagesystem.service.user;

import com.whl.messagesystem.commons.constant.ResultEnum;
import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.dao.UserAdminDao;
import com.whl.messagesystem.dao.UserDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.SessionInfo;
import com.whl.messagesystem.model.entity.User;
import com.whl.messagesystem.model.entity.UserAdmin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static com.whl.messagesystem.commons.constant.StringConstant.SESSION_INFO;

/**
 * @author whl
 * @date 2021/12/7 18:48
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserDao userDao;

    @Resource
    UserAdminDao userAdminDao;

    /**
     * 新用户注册
     */
    @Override
    public ResponseEntity<Result> register(String userName, String password, String adminId) {
        try {
            if (StringUtils.isAnyEmpty(userName, password, adminId)) {
                throw new NullPointerException("参数为空");
            }

            User user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            log.info("要插入的用户信息为: {}", user);

            if (userDao.getUserCountByUserName(userName) > 0) {
                log.error("用户已存在");
                return ResponseEntity.ok(new Result(ResultEnum.ERROR.getStatus(), "用户已存在", null));
            }

            if (userDao.insertAnUser(user)) {
                String userId = userDao.getUserIdWithName(userName);
                UserAdmin userAdmin = new UserAdmin(userId, adminId);
                if (userAdminDao.insertAnUserAdmin(userAdmin)) {
                    log.info("用户注册成功");
                    return ResponseEntity.ok(ResultUtil.success());
                }
            }

            throw new SQLException("user表插入新用户失败 || user_admin表插入关系失败");
        } catch (Exception e) {
            log.error("注册失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    /**
     * 更新用户名、密码
     */
    @Override
    public ResponseEntity<Result> updateUserNameAndPassword(String userId, String userName, String password, HttpSession session) {
        try {
            if (StringUtils.isAnyEmpty(userId, userName, password)) {
                throw new NullPointerException("参数为空");
            }

            User user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setPassword(password);
            log.info("更新的用户信息: {}", user);
            if (userDao.updateUserNameAndPassword(user)) {
                // 更新会话
                SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                sessionInfo.setUser(user);
                session.setAttribute(SESSION_INFO, sessionInfo);

                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user表更新用户记录失败");
        } catch (Exception e) {
            log.error("更新用户信息失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @Override
    public ResponseEntity<Result> logicalDeleteUser(int userId) {
        try {
            if (userId == 0) {
                throw new NullPointerException("参数为空");
            }

            log.info("逻辑删除的用户id为: {}", userId);
            if (userDao.logicalDeleteAnUser(userId)) {
                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user表逻辑删除用户失败");
        } catch (Exception e) {
            log.error("逻辑删除用户失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    /**
     * 永久删除用户
     *
     * @param userIds
     */
    @Override
    public ResponseEntity<Result> completelyDeleteUser(int[] userIds) {
        try {
            if (ArrayUtils.isEmpty(userIds)) {
                throw new NullPointerException("参数为空");
            }

            log.info("永久删除的用户id为: {}", Arrays.toString(userIds));
            if (userDao.completelyDeleteUsers(userIds)) {
                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user表永久删除用户失败");
        } catch (Exception e) {
            log.error("永久删除用户失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> recoverUser(int[] userIds) {
        try {
            if (ArrayUtils.isEmpty(userIds)) {
                throw new NullPointerException("参数为空");
            }

            log.info("恢复的用户id为: {}", Arrays.toString(userIds));
            if (userDao.recoverUsers(userIds)) {
                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user表恢复用户失败");
        } catch (Exception e) {
            log.error("恢复用户失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> listUsersByAdminId(String adminId) {
        try {
            if (StringUtils.isEmpty(adminId)) {
                throw new NullPointerException("参数为空");
            }

            List<User> users = userDao.selectUsersWithAdminId(Integer.parseInt(adminId));
            return ResponseEntity.ok(ResultUtil.success(users));
        } catch (Exception e) {
            log.error("查询用户列表失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> listUsersWithoutAdmin() {
        try {
            List<User> users = userDao.selectUsersWithoutAdmin();
            return ResponseEntity.ok(ResultUtil.success(users));
        } catch (Exception e) {
            log.error("查询未指定管理员的用户列表失败: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }
}
