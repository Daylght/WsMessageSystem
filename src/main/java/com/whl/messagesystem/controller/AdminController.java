package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.AdminInfo;
import com.whl.messagesystem.model.dto.AdminRegisterDTO;
import com.whl.messagesystem.service.admin.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2022/1/22 11:22
 */
@Api("管理员")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    AdminService adminService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAdminList() {
        return adminService.getAdminList();
    }

    @ApiOperation("管理员注册(管理员)")
    @PostMapping("/register")
    public ResponseEntity<Result> register(@RequestBody AdminRegisterDTO adminRegisterDTO) {
        return adminService.register(adminRegisterDTO);
    }

    @ApiOperation("注销管理员账号(管理员)")
    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<Result> deleteAdmin(@PathVariable("adminId") String adminId, HttpSession session) {
        return adminService.deleteAdmin(adminId, session);
    }

    @ApiOperation("更新管理员的用户名与密码(管理员)")
    @PutMapping("/updateNameAndPassword")
    public ResponseEntity<Result> updateAdminNameAndPassword(@RequestBody AdminInfo adminInfo, HttpSession session) {
        return adminService.updateAdminNameAndPassword(adminInfo, session);
    }

    @ApiOperation("选择管理员(用户)")
    @PostMapping("/choice/{adminId}")
    public ResponseEntity<Result> choiceAnAdmin(@PathVariable("adminId") String adminId, HttpSession session) {
        return adminService.choiceAnAdmin(adminId, session);
    }

}
