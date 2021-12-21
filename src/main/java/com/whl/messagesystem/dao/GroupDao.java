package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.Group;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author whl
 * @date 2021/12/21 21:49
 */
@Mapper
public interface GroupDao {
    /**
     * 根据组名在group表中查找一条记录
     * @param groupName
     * @return
     */
    Group findGroupByGroupName(String groupName);
}
