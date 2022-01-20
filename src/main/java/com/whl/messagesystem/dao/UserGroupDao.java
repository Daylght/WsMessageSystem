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
}
