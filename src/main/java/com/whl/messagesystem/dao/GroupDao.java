package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.PublicGroup;
import com.whl.messagesystem.model.entity.User;
import com.whl.messagesystem.model.vo.GroupVO;
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
    boolean insertAGroup(@Param("groupName") String groupName, @Param("creatorId") String creatorId, @Param("adminId") String adminId, @Param("maxCount") int maxCount, @Param("adminCreated") boolean adminCreated);

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
    List<GroupVO> selectAllGroupsAndCreators();

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

    /**
     * 根据创建人id查询组
     * @param creatorId
     * @return
     */
    Group selectGroupByCreatorId(int creatorId);

    /**
     * 根据管理员id查询所有的分组以及他们的创建者
     * @param adminId
     * @return
     */
    List<GroupVO> selectAllGroupsAndCreatorsByAdminId(int adminId);

    /**
     * 根据组id删除这个分组
     * @param groupId
     * @return
     */
    boolean deleteGroupByGroupId(int groupId);

    /**
     * 查询未指定管理员的私有分组以及创建者信息
     * @return
     */
    List<GroupVO> selectAllGroupsAndCreatorsWithoutAdmin();

    /**
     * 根据groupId查询分组的详细信息
     * @param groupId
     * @return
     */
    GroupVO selectGroupVOByGroupId(int groupId);

    /**
     * 根据指定的groupId清空admin_id列的数据
     * @param groupId
     * @return
     */
    boolean clearAdminId(int groupId);

    /**
     * 根据管理员id查询管理员创建的小组
     * @param groupId
     * @return
     */
    List<GroupVO> selectGroupVOWithoutCreatorByAdminId(int groupId);
}
