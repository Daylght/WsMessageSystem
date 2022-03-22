package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.UserAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author whl
 * @date 2022/1/22 12:26
 */
@Mapper
public interface UserAdminDao {

    /**
     * 在user_admin表中插入一条关系
     */
    boolean insertAnUserAdmin(@Param("userAdmin") UserAdmin userAdmin);

    /**
     * 在user_admin表中根据adminId删除所有的关系
     * @param adminId
     * @return
     */
    int deleteUserAdminsByAdminId(int adminId);

    /**
     * 在user_admin表中根据userId删除指定关系
     * @param userId
     * @return
     */
    boolean deleteUserAdminByUserId(int userId);
}
