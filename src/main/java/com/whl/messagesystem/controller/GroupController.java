package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.CreateGroupDto;
import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.UserGroup;
import com.whl.messagesystem.service.group.GroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author whl
 * @date 2022/1/19 20:20
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Resource
    private GroupService groupService;

    @ApiOperation("创建分组(用户)(管理员)")
    @PostMapping("/create")
    public ResponseEntity<Result> createGroup(@RequestBody CreateGroupDto createGroupDto) {
        return groupService.createGroup(createGroupDto);
    }

    @ApiOperation("加入分组(用户)")
    @PostMapping("/join")
    public ResponseEntity<Result> joinGroup(@RequestBody UserGroup userGroup) {
        return groupService.joinGroup(userGroup);
    }

    @ApiOperation("获取分组列表(用户)(管理员)")
    @GetMapping("/list")
    public ResponseEntity<Result> getGroupsList() {
        return groupService.getGroupsList();
    }

    @ApiOperation("删除分组(用户)(管理员)")
    @DeleteMapping("/remove")
    public ResponseEntity<Result> remove(@RequestBody int[] groupIds) {
        return groupService.remove(groupIds);
    }

    @ApiOperation("修改分组信息(用户)(管理员)")
    @PutMapping("/update")
    public ResponseEntity<Result> updateGroupInfo(@RequestBody Group group) {
        return groupService.updateGroupInfo(group);
    }

}
