package com.whl.messagesystem.service.group;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.CreateGroupDTO;
import com.whl.messagesystem.model.dto.CreatePublicGroupDTO;
import com.whl.messagesystem.model.dto.OutsideCreatePublicGroupDTO;
import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.UserGroup;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/21 22:02
 */
public interface GroupService {

    /**
     * 根据groupName判断组是否存在
     *
     * @param groupName
     * @return
     */
    boolean isExistGroup(String groupName);

    /**
     * 创建一个新分组
     *
     * @param createGroupDto
     * @return
     */
    ResponseEntity<Result> createGroup(CreateGroupDTO createGroupDto, HttpSession session);

    /**
     * 获取所有分组的列表
     *
     * @return
     */
    ResponseEntity<Result> getGroupsList();

    /**
     * 根据数组中提供的groupId批量删除分组<br>
     * ps: 数组中仅有一个id也是可以的
     *
     * @param groupIds
     * @return
     */
    ResponseEntity<Result> remove(int[] groupIds, HttpSession session);

    /**
     * 修改组的信息
     *
     * @param group
     * @return
     */
    ResponseEntity<Result> updateGroupInfo(Group group);

    /**
     * 指定用户加入指定的组
     *
     * @param userGroup
     * @return
     */
    ResponseEntity<Result> joinGroup(UserGroup userGroup, HttpSession session);

    /**
     * 根据指定组id获取组员列表
     *
     * @param groupId
     * @return
     */
    ResponseEntity<Result> listGroupMembers(String groupId, HttpSession session);

    /**
     * 使指定的用户退出分组
     *
     * @param userId
     * @return
     */
    ResponseEntity<Result> quitGroup(int userId, HttpSession session);

    /**
     * 根据管理员id获取分组列表
     * @param adminId
     * @return
     */
    ResponseEntity<Result> getGroupsListByAdminId(String adminId);

    /**
     * 创建外部分组，供外部进行消息实时互通使用
     * @param createPublicGroupDTO
     * @return
     */
    ResponseEntity<Result> createPublicGroup(CreatePublicGroupDTO createPublicGroupDTO);

    /**
     * 根据用户id踢出组员
     * @param userId
     * @return
     */
    ResponseEntity<Result> kickGroupMember(String userId);

    /**
     * 根据指定的管理员id，获取这个管理员创建的公共分组列表
     * @param adminId
     * @return
     */
    ResponseEntity<Result> listPublicGroupsCreatedByAdmin(String adminId);

    /**
     * 获取以外部调用形式创建的公共分组列表
     * @return
     */
    ResponseEntity<Result> listPublicGroupsCreatedByOutside();

    /**
     * 根据传入的组id解散分组
     * @param groupId
     * @param session
     * @return
     */
    ResponseEntity<Result> dismissGroup(String groupId, HttpSession session);

    /**
     * 根据组id解散指定的公共分组
     * @param groupId
     * @return
     */
    ResponseEntity<Result> dismissPublicGroup(String groupId);

    /**
     * 由外部调用创建公共分组
     * @param outsideCreatePublicGroupDTO
     * @return
     */
    ResponseEntity<Result> outsideCreatePublicGroup(OutsideCreatePublicGroupDTO outsideCreatePublicGroupDTO);

    /**
     * 获取未指定管理员的私有分组列表
     * @return
     */
    ResponseEntity<Result> listGroupsWithoutAdmin();

    /**
     * 由管理员创建内部分组
     * @param createGroupDTO
     * @return
     */
    ResponseEntity<Result> adminCreateGroup(CreateGroupDTO createGroupDTO);
}
