package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.UserRegisterDto;
import com.whl.messagesystem.model.dto.UserUpdateDto;
import com.whl.messagesystem.model.entity.User;
import com.whl.messagesystem.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author whl
 * @date 2021/12/7 15:29
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    UserService userService;

    /**
     * 注册新用户
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateUserInfo(userUpdateDto);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/deleteUser/{name}")
    public Result deleteUser(String userName) {
        return userService.deleteUser(userName);
    }

}
