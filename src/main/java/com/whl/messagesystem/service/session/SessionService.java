package com.whl.messagesystem.service.session;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.LoginDTO;
import com.whl.messagesystem.model.entity.Admin;
import com.whl.messagesystem.model.entity.Group;
import org.springframework.http.ResponseEntity;

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
    ResponseEntity<Result> getSessionInfo(HttpSession session);

    /**
     * 用户登录
     */
    ResponseEntity<Result> userLogin(LoginDTO loginDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 登出，销毁当前会话
     */
    ResponseEntity<Result> logout(HttpSession session);

    /**
     * 生成验证码并返回图片
     */
    void generateVerifyCode(HttpServletRequest request, HttpServletResponse response);

    /**
     * 管理员登录
     * @param loginDto
     * @param request
     * @param response
     * @return
     */
    ResponseEntity<Result> adminLogin(LoginDTO loginDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 移除sessionInfo中的分组信息
     * @param session
     * @return
     */
    ResponseEntity<Result> removeGroupInfoFromSession(HttpSession session);

    /**
     * 移除sessionInfo中的管理员信息
     * @param session
     * @return
     */
    ResponseEntity<Result> removeAdminInfoFromSession(HttpSession session);

    /**
     * 设置sessionInfo中的管理员信息
     * @param admin
     * @param session
     * @return
     */
    ResponseEntity<Result> setAdminInfoOnSession(Admin admin, HttpSession session);
}
