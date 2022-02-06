package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.User;
import com.whl.messagesystem.model.vo.GroupVo;
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
     * 根据组id在group表中查找一条记录
     * @param groupId
     * @return
     */
    Group selectGroupByGroupId(int groupId);

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

    /**
     * 根据userId查询用户所在的组
     * @param userId
     * @return
     */
    Group selectGroupByUserId(int userId);

    /**
     * 查询所有的分组以及他们的创建者
     * @return
     */
    List<GroupVo> selectAllGroupsAndCreators();

    /**
     * 根据创建人id查询他创建的组的个数
     * @param creatorId
     * @return
     */
    int selectGroupCountByCreatorId(int creatorId);

    /**
     * 根据组id查询组内的成员，以list形式返回
     * @param groupId
     * @return
     */
    List<User> selectUsersWithGroupId(int groupId);
}
