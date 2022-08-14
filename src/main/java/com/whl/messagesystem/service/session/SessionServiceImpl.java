package com.whl.messagesystem.service.session;

import com.whl.messagesystem.commons.constant.LoginResultConstant;
import com.whl.messagesystem.commons.constant.ResultEnum;
import com.whl.messagesystem.commons.constant.RoleConstant;
import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.commons.utils.verifyCode.IVerifyCodeGen;
import com.whl.messagesystem.dao.AdminDao;
import com.whl.messagesystem.dao.GroupDao;
import com.whl.messagesystem.dao.UserDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.VerifyCode;
import com.whl.messagesystem.model.dto.LoginDTO;
import com.whl.messagesystem.model.dto.SessionInfo;
import com.whl.messagesystem.model.entity.Admin;
import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

import static com.whl.messagesystem.commons.constant.StringConstant.SESSION_INFO;

/**
 * @author whl
 * @date 2021/12/7 18:47
 */
@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

    @Resource
    private IVerifyCodeGen iVerifyCodeGen;

    @Resource
    private UserDao userDao;

    @Resource
    private GroupDao groupDao;

    @Resource
    private AdminDao adminDao;

    /**
     * 获取当前会话的信息
     */
    @Override
    public ResponseEntity<Result> getSessionInfo(HttpSession session) {
        try {
            SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
            if (sessionInfo == null) {
                Result result = new Result(ResultEnum.ERROR.getStatus(), "未登录", null);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
            }

            return ResponseEntity.ok(ResultUtil.success(sessionInfo));
        } catch (Exception e) {
            log.error("获取当前用户信息异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    /**
     * 用户登录<br>
     * 需要注意，这个方法只会查询出未被逻辑删除的用户，即是说show_status为1的是无法登录成功的
     */
    @Override
    public ResponseEntity<Result> userLogin(LoginDTO loginDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();

            // 没太弄懂这行的作用，也许是在登录之前确保之前的账号已经被登出了
            session.removeAttribute(SESSION_INFO);

            // 判断用户输入的验证码是否正确
            log.info("登录所用的sessionId: {}", session.getId());
            String sessionVerifyCode = (String) session.getAttribute("VerifyCode");
            sessionVerifyCode = sessionVerifyCode.toLowerCase();
            if (!loginDto.getServerCode().toLowerCase().equals(sessionVerifyCode)) {
                log.error("验证码错误");
                Result result = new Result(LoginResultConstant.WRONG_VERIFYCODE.getCode(), LoginResultConstant.WRONG_VERIFYCODE.getMessage(), null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            } else {

                /*
                  在user表中进行查找，若找到了就表示登录成功，且把用户信息放入session中
                 */
                String userName = loginDto.getLoginName();
                String password = loginDto.getPassword();

                User user = userDao.getActiveUserWithName(userName);

                if (user != null) {

                    if (user.getPassword().equals(password)) {
                        Group group = groupDao.selectGroupByUserId(Integer.parseInt(user.getUserId()));

                        /*
                         如果在user_group表中没查询到关系，说明当前用户可能是组长，也可能没加入或创建组
                         判断下当前用户是不是组长，若是则给group赋值
                         */
                        if (group == null) {
                            group = groupDao.selectGroupByCreatorId(Integer.parseInt(user.getUserId()));
                        }

                        Admin admin = adminDao.selectAdminByUserId(Integer.parseInt(user.getUserId()));

                        SessionInfo sessionInfo = new SessionInfo();
                        sessionInfo.setRole(RoleConstant.USER);
                        sessionInfo.setUser(user);
                        sessionInfo.setGroup(group);
                        sessionInfo.setAdmin(admin);
                        session.setAttribute(SESSION_INFO, sessionInfo);

                        // 如果group是null，则说明当前用户不在组中；否则在组中，应该在登录后自动进入组内
                        return ResponseEntity.ok(ResultUtil.success(group));
                    } else {
                        log.error("密码错误");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Result(ResultEnum.ERROR.getStatus(), "密码错误", null));
                    }

                } else {
                    log.error("用户不存在");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Result(ResultEnum.ERROR.getStatus(), "用户不存在", null));
                }

            }
        } catch (Exception e) {
            log.error("登录异常: {}", e.getMessage());
            log.error("堆栈信息: {0}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> adminLogin(LoginDTO loginDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();

            // 没太弄懂这行的作用，也许是在登录之前确保之前的账号已经被登出了
            session.removeAttribute(SESSION_INFO);

            // 判断用户输入的验证码是否正确
            String sessionVerifyCode = (String) session.getAttribute("VerifyCode");
            sessionVerifyCode = sessionVerifyCode.toLowerCase();
            if (!loginDto.getServerCode().toLowerCase().equals(sessionVerifyCode)) {
                log.error("验证码错误");
                Result result = new Result(LoginResultConstant.WRONG_VERIFYCODE.getCode(), LoginResultConstant.WRONG_VERIFYCODE.getMessage(), null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            } else {

                /*
                  在admin表中进行查找，若找到了就表示登录成功，且把管理员信息放入session中
                 */
                String adminName = loginDto.getLoginName();
                String password = loginDto.getPassword();

                Admin admin = adminDao.selectAdminByAdminName(adminName);

                if (admin != null) {

                    if (admin.getPassword().equals(password)) {
                        SessionInfo sessionInfo = new SessionInfo();
                        sessionInfo.setRole(RoleConstant.ADMIN);
                        sessionInfo.setAdmin(admin);
                        session.setAttribute(SESSION_INFO, sessionInfo);

                        return ResponseEntity.ok(ResultUtil.success(admin.getAdminId()));
                    } else {
                        log.error("密码错误");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Result(ResultEnum.ERROR.getStatus(), "密码错误", null));
                    }

                } else {
                    log.error("管理员不存在");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Result(ResultEnum.ERROR.getStatus(), "管理员不存在", null));
                }

            }
        } catch (Exception e) {
            log.error("登录异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    /**
     * 登出，销毁当前会话
     */
    @Override
    public ResponseEntity<Result> logout(HttpSession session) {
        try {
            session.removeAttribute(SESSION_INFO);
            return ResponseEntity.ok(ResultUtil.success());
        } catch (Exception e) {
            log.error("登出异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    /**
     * 生成验证码并返回图片
     */
    @Override
    public void generateVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            //设置长宽
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();
            log.info("验证码: {}", code);
            log.info("获取验证码的sessionId: {}", request.getSession().getId());
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
            log.info(Arrays.toString(request.getCookies()));
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error("生成验证码失败: {}", e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Result> removeGroupInfoFromSession(HttpSession session) {
        try {
            SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
            sessionInfo.setGroup(null);
            return ResponseEntity.ok(ResultUtil.success(sessionInfo));
        } catch (Exception e) {
            log.error("从sessionInfo中移除分组信息异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> removeAdminInfoFromSession(HttpSession session) {
        try {
            SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
            sessionInfo.setAdmin(null);
            return ResponseEntity.ok(ResultUtil.success(sessionInfo));
        } catch (Exception e) {
            log.error("从sessionInfo中移除管理员信息异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> setAdminInfoOnSession(Admin admin, HttpSession session) {
        try {
            SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
            sessionInfo.setAdmin(admin);
            return ResponseEntity.ok(ResultUtil.success());
        } catch (Exception e) {
            log.error("设置sessionInfo中的管理员信息异常，参数：{}，异常信息： {}", admin, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }
}
