package com.whl.messagesystem.service.session;

import com.whl.messagesystem.commons.utils.verifyCode.IVerifyCodeGen;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.VerifyCode;
import com.whl.messagesystem.model.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author whl
 * @date 2021/12/7 18:47
 */
@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

    @Resource
    IVerifyCodeGen iVerifyCodeGen;

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

    /**
     * 生成验证码并返回图片
     *
     * @param request
     * @param response
     */
    @Override
    public void generateVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            //设置长宽
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();
            log.info("验证码: {}", code);
            //将VerifyCode绑定session
            request.getSession().setAttribute("VerifyCode", code);
            //设置响应头
            response.setHeader("Pragma", "no-cache");
            //设置响应头
            response.setHeader("Cache-Control", "no-cache");
            //在代理服务器端防止缓冲
            response.setDateHeader("Expires", 0);
            //设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error("生成验证码失败: {}", e.getMessage());
        }
    }
}
