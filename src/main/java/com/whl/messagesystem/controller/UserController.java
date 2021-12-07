package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author whl
 * @date 2021/12/7 15:29
 */
@RequestMapping("/user")
@RestController
public class UserController {

    /**
     * 注册新用户
     */
    @PostMapping("/")
    public Result register() {  // todo:添加dto作为参数
        return null;
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{name}")
    public Result userInfo(String userName) {
        return null;
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{name}")
    public Result updateUserInfo(String userName) {
        return null;
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{name}")
    public Result deleteUser(String userName) {
        return null;
    }

}
