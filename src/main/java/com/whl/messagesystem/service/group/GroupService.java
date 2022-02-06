package com.whl.messagesystem.service.group;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.CreateGroupDto;
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
    ResponseEntity<Result> createGroup(CreateGroupDto createGroupDto);

    /**
     * 获取所有分组的列表
     *
     * @return
     */
    ResponseEntity<Result> getGroupsList();

    /**
     * 根据数组中提供的groupId批量删除分组<br>
     * ps: 数组中仅有一个id也是可以的
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
     * @param groupId
     * @return
     */
    ResponseEntity<Result> listGroupMembers(String groupId, HttpSession session);
}
