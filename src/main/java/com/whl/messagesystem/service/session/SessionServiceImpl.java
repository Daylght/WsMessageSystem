package com.whl.messagesystem.service.session;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.LoginDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/7 18:47
 */
@Service
public class SessionServiceImpl implements SessionService {
    /**
     * 获取当前会话的信息
     *
     * @param session
     */
    @Override
    public Result getSessionInfo(HttpSession session) {
        return null;
    }

    /**
     * 用户登录
     *
     * @param loginDto
     * @param request
     * @param response
     */
    @Override
    public Result login(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    /**
     * 更新会话信息
     *
     * @param session
     */
    @Override
    public Result updateSession(HttpSession session) {
        return null;
    }

    /**
     * 登出，销毁当前会话
     *
     * @param session
     */
    @Override
    public Result logout(HttpSession session) {
        return null;
    }
}
