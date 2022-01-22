package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.LoginDto;
import com.whl.messagesystem.service.session.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/7 18:36
 */
@Api("会话")
@RequestMapping("/session")
@RestController
public class SessionController {

    @Resource
    private SessionService sessionService;

    /**
     * 获取当前会话的信息(当前用户基本信息、所在分组等)
     * 这个接口的原则: 无论前端什么时候调用，都应该返回正确的信息，即sessionInfo中的信息需要保持最新
     */
    @ApiOperation("获取当前会话的信息(用户)(管理员)")
    @GetMapping("/getSessionInfo")
    public ResponseEntity<Result> sessionInfo(HttpSession session) {
        return sessionService.getSessionInfo(session);
    }

    /**
     * 用户登录
     */
    @ApiOperation("用户登录(用户)")
    @PostMapping("/userLogin")
    public ResponseEntity<Result> userLogin(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        return sessionService.userLogin(loginDto, request, response);
    }

    /**
     * 登出，销毁当前会话
     */
    @ApiOperation("登出，销毁当前会话(用户)")
    @DeleteMapping("/logout")
    public ResponseEntity<Result> logout(HttpSession session) {
        return sessionService.logout(session);
    }

    /**
     * 生成验证码
     */
    @ApiOperation("生成验证码(用户)")
    @GetMapping("/getVerifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        sessionService.generateVerifyCode(request, response);
    }

}
