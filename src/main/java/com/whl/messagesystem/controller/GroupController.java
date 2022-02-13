package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.CreateGroupDto;
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

    @ApiOperation("创建分组(用户)(管理员)")
    @PostMapping("/create")
    public ResponseEntity<Result> createGroup(@RequestBody CreateGroupDto createGroupDto, HttpSession session) {
        return groupService.createGroup(createGroupDto, session);
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

    @ApiOperation("获取指定组的成员列表")
    @GetMapping("/listMembers/{groupId}")
    public ResponseEntity<Result> listGroupMembers(@PathVariable("groupId") String groupId, HttpSession session) {
        return groupService.listGroupMembers(groupId, session);
    }

}
