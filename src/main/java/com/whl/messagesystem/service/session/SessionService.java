package com.whl.messagesystem.service.session;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.LoginDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/7 18:46
 */
public interface SessionService {
    /**
     * 获取当前会话的信息
     */
    Result getSessionInfo(HttpSession session);

    /**
     * 用户登录
     */
    Result userLogin(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 登出，销毁当前会话
     */
    Result logout(HttpSession session);

    /**
     * 生成验证码并返回图片
     */
    void generateVerifyCode(HttpServletRequest request, HttpServletResponse response);
}
