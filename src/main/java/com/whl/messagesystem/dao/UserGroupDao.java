package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.UserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whl
 * @date 2022/1/20 17:45
 */
@Mapper
public interface UserGroupDao {

    /**
     * 在user_gorup表中插入一条关系
     * @param userGroup
     * @return
     */
    boolean insertAnUserGroup(@Param("userGroup") UserGroup userGroup);

    /**
     * 根据userId查询关系的数量
     * @param userId
     * @return
     */
    int selectUserGroupCountByUserId(int userId);

    /**
     * 根据userId删除关系
     * @param userId
     * @return
     */
    boolean deleteAnUserGroupByUserId(int userId);

    /**
     * 根据groupId删除这个组所有的成员关系
     * @param groupId
     * @return
     */
    int deleteUserGroupsByGroupId(int groupId);

    /**
     * 根据分组id查询所有的关系
     * @param groupId
     * @return
     */
    List<UserGroup> selectUserGroupsByGroupId(int groupId);

    /**
     * 根据userId查询一条关系
     * @param userId
     * @return
     */
    UserGroup selectUserGroupByUserId(int userId);
}
