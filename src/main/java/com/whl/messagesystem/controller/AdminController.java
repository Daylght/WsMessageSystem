package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.service.admin.AdminService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}
