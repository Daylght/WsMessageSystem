package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.PublicGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    boolean insertPublicGroup(@Param("publicGroup") PublicGroup publicGroup);

    /**
     * 在public_group表中根据组名查找一条记录
     * @param groupName
     * @return
     */
    PublicGroup selectPublicGroupByName(String groupName);

    /**
     * 根据管理员id查询他创建的公共分组列表
     * @param adminId
     * @return
     */
    List<PublicGroup> selectPublicGroupsWithAdminId(int adminId);

    /**
     * 获取由外部调用创建的公共分组列表
     * @return
     */
    List<PublicGroup> selectPublicGroupsCreatedByOutside();
}
