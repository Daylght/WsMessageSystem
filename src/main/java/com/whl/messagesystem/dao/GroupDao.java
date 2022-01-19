package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 插入一条组记录
     * @param groupName
     * @param creatorId
     * @param adminId
     * @param maxCount
     * @return
     */
    boolean insertAGroup(@Param("groupName") String groupName, @Param("creatorId") String creatorId, @Param("adminId") String adminId, @Param("maxCount") int maxCount);

    /**
     * 查询所有的分组
     * @return
     */
    List<Group> selectAllGroups();

    /**
     * 根据多个groupId批量删除group表中的记录
     * @param groupIds
     * @return
     */
    boolean deleteGroups(int[] groupIds);

    /**
     * 更新一条group记录
     * @param group
     * @return
     */
    int updateGroup(Group group);
}
