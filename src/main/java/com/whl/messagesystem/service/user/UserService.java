package com.whl.messagesystem.service.user;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.entity.UserGroup;
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

    /**
     * 根据管理员id展示用户列表
     * @param adminId
     * 管理员id
     * @return
     * 当前管理员所属的全部用户的列表
     */
    ResponseEntity<Result> listUsersByAdminId(String adminId);

    /**
     * 获取未指定管理员的用户列表
     * @return
     */
    ResponseEntity<Result> listUsersWithoutAdmin();

    /**
     * 管理员放弃管理用户
     * @param userGroup
     * @param session
     * @return
     */
    ResponseEntity<Result> giveUpManageUser(UserGroup userGroup, HttpSession session);

    /**
     * 管理员选择一个未指定管理员的用户进行管理
     * @param userGroup
     * @param session
     * @return
     */
    ResponseEntity<Result> choiceUserToManage(UserGroup userGroup, HttpSession session);
}
