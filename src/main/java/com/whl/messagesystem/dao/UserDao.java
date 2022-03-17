package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whl
 * @date 2021/12/7 23:14
 */
@Mapper
public interface UserDao {
    /**
     * 往user表中插入一条新的记录
     */
    Boolean insertAnUser(User user);

    /**
     * 更新user表中的一条记录
     * @param user
     * @return
     */
    Boolean updateUserNameAndPassword(User user);

    /**
     * 逻辑删除一个用户，即把user表中某条记录的showStatus置为1
     */
    Boolean logicalDeleteAnUser(int userId);

    /**
     * 在user表中根据userName和password查找表中能与之对应的未被删除的学生数量
     * 这个方法在登录中使用
     */
    int getActiveUsersCountWithNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    String getUserIdWithName(String userName);

    /**
     * 在user表中根据userName查找表中能与之对应的学生数量
     * 这个方法在注册中使用
     */
    int getUserCountByUserName(String userName);

    /**
     * 在user表中根据userName查找表中能与之对应的未被删除的学生信息
     */
    User getActiveUserWithName(String userName);

    /**
     * 根据userIds批量删除user记录
     */
    boolean completelyDeleteUsers(int[] userIds);

    /**
     * 把指定userIds的记录的show_status批量置为0
     */
    boolean recoverUsers(int[] userIds);

    /**
     * 根据指定的userId查询学生信息
     */
    User selectUserWithUserId(int userId);

    /**
     * 在user表中根据管理员id查询用户列表,以list形式返回
     * @param adminId
     * @return
     */
    List<User> selectUsersWithAdminId(int adminId);

    /**
     * 在user表中查询未指定管理员的用户列表
     * @return
     */
    List<User> selectUsersWithoutAdmin();
}
