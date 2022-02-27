package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.PublicGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author whl
 * @date 2022/2/27 17:53
 */
@Mapper
public interface PublicGroupDao {
    /**
     * 在public_group表中根据组名查询对应的组的数量
     * @param groupName
     * @return
     */
    Integer selectPublicGroupCountByName(String groupName);

    /**
     * 在public_group表中插入一条新的记录
     * @param publicGroup
     * @return
     */
    boolean insertPublicGroup(PublicGroup publicGroup);

    /**
     * 在public_group表中根据组名查找一条记录
     * @param groupName
     * @return
     */
    PublicGroup selectPublicGroupByName(String groupName);
}
