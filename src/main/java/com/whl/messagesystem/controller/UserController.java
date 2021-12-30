package com.whl.messagesystem.controller;

import com.whl.messagesystem.commons.constant.ResultEnum;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import com.whl.messagesystem.model.dto.UserUpdateDto;
import com.whl.messagesystem.service.user.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
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
    @ApiOperation("注册新用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query")
    })
    @PostMapping("/register")
    public Result register(String userName, String password) {
        return userService.register(userName, password);
    }

    /**
     * 更新用户名、密码
     */
    @ApiOperation("更新用户名、密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query")
    })
    @PutMapping("/updateNameAndPassword")
    public Result updateUserNameAndPassword(String userId, String userName, String password, HttpSession session) {
        return userService.updateUserNameAndPassword(userId, userName, password, session);
    }

    /**
     * 逻辑删除用户
     */
    @ApiOperation("逻辑删除用户")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query")
    @DeleteMapping("/deleteUser")
    public Result logicalDeleteUser(String userId) {
        return userService.deleteUser(userId);
    }

}
