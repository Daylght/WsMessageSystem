package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.UserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author whl
 * @date 2022/1/20 17:45
 */
@Mapper
public interface UserGroupDao {
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
    boolean deleteUserGroupsByGroupId(int groupId);
}
