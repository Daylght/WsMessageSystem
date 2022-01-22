package com.whl.messagesystem.service.user;

import com.whl.messagesystem.model.Result;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/7 18:47
 */
public interface UserService {
    /**
     * 新用户注册
     */
    ResponseEntity<Result> register(String userName, String password, String adminId);

    /**
     * 更新用户名、密码
     */
    ResponseEntity<Result> updateUserNameAndPassword(String userId, String userName, String password, HttpSession session);

    /**
     * 逻辑删除用户
     */
    ResponseEntity<Result> logicalDeleteUser(int userId);

    /**
     * 永久删除用户
     */
    ResponseEntity<Result> completelyDeleteUser(int[] userIds);

    /**
     * 恢复被逻辑删除的用户
     */
    ResponseEntity<Result> recoverUser(int[] userIds);
}
