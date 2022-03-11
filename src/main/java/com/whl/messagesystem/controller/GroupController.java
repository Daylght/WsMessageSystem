package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.CreateGroupDTO;
import com.whl.messagesystem.model.dto.CreatePublicGroupDTO;
import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.UserGroup;
import com.whl.messagesystem.service.group.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2022/1/19 20:20
 */
@Api("分组")
@RestController
@RequestMapping("/group")
public class GroupController {

    @Resource
    private GroupService groupService;

    @ApiOperation("创建内部分组(用户)(管理员)")
    @PostMapping("/create")
    public ResponseEntity<Result> createGroup(@RequestBody CreateGroupDTO createGroupDto, HttpSession session) {
        return groupService.createGroup(createGroupDto, session);
    }

    @ApiOperation("创建外部分组(管理员)(外部调用)")
    @PostMapping("/public/create")
    public ResponseEntity<Result> createPublicGroup(@RequestBody CreatePublicGroupDTO createPublicGroupDTO) {
        return groupService.createPublicGroup(createPublicGroupDTO);
    }

    @ApiOperation("根据管理员id获取他创建的公共分组列表(管理员)")
    @GetMapping("/public/{adminId}")
    public ResponseEntity<Result> listPublicGroupsCreatedByAdmin(@PathVariable("adminId") String adminId) {
        return groupService.listPublicGroupsCreatedByAdmin(adminId);
    }

    @ApiOperation("加入分组(用户)")
    @PostMapping("/join")
    public ResponseEntity<Result> joinGroup(@RequestBody UserGroup userGroup, HttpSession session) {
        return groupService.joinGroup(userGroup, session);
    }

    @ApiOperation("退出分组(用户)")
    @DeleteMapping("/quit/{userId}")
    public ResponseEntity<Result> quitGroup(@PathVariable("userId") String userId, HttpSession session) {
        return groupService.quitGroup(Integer.parseInt(userId), session);
    }

    @ApiOperation("踢出分组内的成员")
    @DeleteMapping("/kick/{userId}")
    public ResponseEntity<Result> kickGroupMember(@PathVariable("userId") String userId) {
        return groupService.kickGroupMember(userId);
    }

    @ApiOperation("获取分组列表(用户)(管理员)")
    @GetMapping("/list")
    public ResponseEntity<Result> getGroupsList() {
        return groupService.getGroupsList();
    }

    @ApiOperation("根据管理员id获取分组列表(用户)(管理员)")
    @GetMapping("list/{adminId}")
    public ResponseEntity<Result> getGroupsListByAdminId(@PathVariable("adminId") String adminId) {
        return groupService.getGroupsListByAdminId(adminId);
    }

    @ApiOperation("删除分组(用户)(管理员)")
    @DeleteMapping("/remove/{groupIds}")
    public ResponseEntity<Result> remove(@PathVariable("groupIds") int[] groupIds, HttpSession session) {
        return groupService.remove(groupIds, session);
    }

    @ApiOperation("修改分组信息(用户)(管理员)")
    @PutMapping("/update")
    public ResponseEntity<Result> updateGroupInfo(@RequestBody Group group) {
        return groupService.updateGroupInfo(group);
    }

    @ApiOperation("获取指定组的成员列表(用户)(管理员)")
    @GetMapping("/listMembers/{groupId}")
    public ResponseEntity<Result> listGroupMembers(@PathVariable("groupId") String groupId, HttpSession session) {
        return groupService.listGroupMembers(groupId, session);
    }

    @ApiOperation("获取外部创建的公共分组列表(管理员)")
    @GetMapping("/public")
    public ResponseEntity<Result> listPublicGroupsCreatedByOutside() {
        return groupService.listPublicGroupsCreatedByOutside();
    }

    //todo: 完成解散分组的接口

}
