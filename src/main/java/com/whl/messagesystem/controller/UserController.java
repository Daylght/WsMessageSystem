package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserInfo;
import com.whl.messagesystem.model.dto.UserRegisterDTO;
import com.whl.messagesystem.model.entity.UserGroup;
import com.whl.messagesystem.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/7 15:29
 */
@Api("用户")
@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    UserService userService;

    /**
     * 注册新用户
     */
    @ApiOperation("注册新用户(用户)")
    @PostMapping("/register")
    public ResponseEntity<Result> register(@RequestBody UserRegisterDTO userRegisterDto) {
        return userService.register(userRegisterDto.getUserName(), userRegisterDto.getPassword(), userRegisterDto.getAdminId());
    }

    /**
     * 更新用户名、密码
     */
    @ApiOperation("更新用户名、密码(用户)")
    @PutMapping("/updateNameAndPassword")
    public ResponseEntity<Result> updateUserNameAndPassword(@RequestBody UserInfo userInfo, HttpSession session) {
        return userService.updateUserNameAndPassword(userInfo.getUserId(), userInfo.getUserName(), userInfo.getPassword(), session);
    }

    /**
     * 逻辑删除用户
     */
    @ApiOperation("逻辑删除用户(用户)(管理员)")
    @DeleteMapping("/logicalDeleteUser/{userId}")
    public ResponseEntity<Result> logicalDeleteUser(@PathVariable("userId") String userId) {
        return userService.logicalDeleteUser(Integer.parseInt(userId));
    }

    /**
     * 彻底删除用户
     */
    @ApiOperation("彻底删除用户(管理员)")
    @DeleteMapping("/completelyDeleteUser/{userId}")
    public ResponseEntity<Result> completelyDeleteUser(@PathVariable("userId") String userId) {
        return userService.completelyDeleteUser(Integer.parseInt(userId));
    }

    /**
     * 恢复用户
     */
    @ApiOperation("恢复被逻辑删除的用户(管理员)")
    @PutMapping("/recover/{userId}")
    public ResponseEntity<Result> recoverUser(@PathVariable("userId") String userId) {
        return userService.recoverUser(Integer.parseInt(userId));
    }

    /**
     * 根据管理员id展示用户列表
     */
    @ApiOperation("根据管理员id展示用户列表(管理员)")
    @GetMapping("/list/{adminId}")
    public ResponseEntity<Result> listUsersByAdminId(@PathVariable("adminId") String adminId) {
        return userService.listUsersByAdminId(adminId);
    }

    /**
     * 展示未指定管理员的用户列表
     */
    @ApiOperation("展示未指定管理员的用户列表")
    @GetMapping("/listWithoutAdmin")
    public ResponseEntity<Result> listUsersWithoutAdmin() {
        return userService.listUsersWithoutAdmin();
    }

    /**
     * 管理员放弃管理用户
     */
    @ApiOperation("管理员放弃管理用户(管理员)")
    @DeleteMapping("/giveUpManage/{userId}&{groupId}")
    public ResponseEntity<Result> giveUpManageUser(@PathVariable("userId") String userId, @PathVariable("groupId") String groupId, HttpSession session) {
        return userService.giveUpManageUser(new UserGroup(userId, groupId), session);
    }

    /**
     * 管理员选择一个未指定管理员的用户进行管理
     */
    @ApiOperation("管理员选择一个未指定管理员的用户进行管理(管理员)")
    @PostMapping("/choiceUserToManage")
    public ResponseEntity<Result> choiceUserToManage(@RequestBody UserGroup userGroup, HttpSession session) {
        return userService.choiceUserToManage(userGroup, session);
    }

}
