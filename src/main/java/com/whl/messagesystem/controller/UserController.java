package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import com.whl.messagesystem.service.user.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author whl
 * @date 2021/12/7 15:29
 */
@SuppressWarnings("all")
@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    UserService userService;

    /**
     * 注册新用户
     */
    @PostMapping("/")
    public Result register(UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{name}")
    public Result userInfo(String userName) {
        return userService.getUserInfo(userName);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{name}")
    public Result updateUserInfo(String userName) {
        return userService.updateUserInfo(userName);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{name}")
    public Result deleteUser(String userName) {
        return userService.deleteUser(userName);
    }

}
