package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.RegisterDto;
import com.whl.messagesystem.model.dto.UserInfo;
import com.whl.messagesystem.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/7 15:29
 */
@Api("用户")
@Slf4j
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
    public ResponseEntity<Result> register(@RequestBody RegisterDto registerDto) {
        return userService.register(registerDto.getUserName(), registerDto.getPassword());
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
    @ApiOperation("逻辑删除用户(用户)")
    @DeleteMapping("/logicalDeleteUser")
    public ResponseEntity<Result> logicalDeleteUser(@RequestBody int userId) {
        return userService.logicalDeleteUser(userId);
    }

    /**
     * 彻底删除用户
     */
    @ApiOperation("彻底删除用户(管理员)")
    @DeleteMapping("/completelyDeleteUser")
    public ResponseEntity<Result> completelyDeleteUser(@RequestBody int userId) {
        return userService.completelyDeleteUser(userId);
    }

}
