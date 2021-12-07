package com.whl.messagesystem.controller;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.LoginDto;
import com.whl.messagesystem.service.session.SessionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/7 18:36
 */
@SuppressWarnings("all")
@RestController
public class SessionController {

    @Resource
    SessionService sessionService;

    /**
     * 获取当前会话的信息
     */
    @GetMapping("/session")
    public Result sessionInfo(HttpSession session) {
        return sessionService.getSessionInfo(session);
    }

    /**
     * 用户登录
     */
    @PostMapping("/session")
    public Result login(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        return sessionService.login(loginDto, request, response);
    }

    /**
     * 更新会话信息
     */
    @PutMapping("/session")
    public Result updateSession(HttpSession session) {
        return sessionService.updateSession(session);
    }

    /**
     * 登出，销毁当前会话
     */
    @DeleteMapping("/session")
    public Result logout(HttpSession session) {
        return sessionService.logout(session);
    }

}
