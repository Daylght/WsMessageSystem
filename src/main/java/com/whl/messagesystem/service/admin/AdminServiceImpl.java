package com.whl.messagesystem.service.admin;

import com.whl.messagesystem.commons.constant.ResultEnum;
import com.whl.messagesystem.commons.constant.RoleConstant;
import com.whl.messagesystem.commons.constant.StringConstant;
import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.dao.AdminDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.AdminRegisterDTO;
import com.whl.messagesystem.model.dto.SessionInfo;
import com.whl.messagesystem.model.entity.Admin;
import com.whl.messagesystem.service.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author whl
 * @date 2022/1/22 11:25
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Resource
    AdminDao adminDao;

    @Resource
    SessionService sessionService;

    @Override
    public ResponseEntity<Result> getAdminList() {
        try {
            List<Admin> admins = adminDao.selectAllAdminsWithoutPassword();
            return ResponseEntity.ok(ResultUtil.success(admins));
        } catch (Exception e) {
            log.error("获取管理员列表异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> register(AdminRegisterDTO adminRegisterDTO) {
        try {
            if (ObjectUtils.isEmpty(adminRegisterDTO)) {
                throw new NullPointerException("参数为空");
            }

            String adminName = adminRegisterDTO.getAdminName();
            String password = adminRegisterDTO.getPassword();
            Admin admin = new Admin();
            admin.setAdminName(adminName);
            admin.setPassword(password);
            log.info("要插入的管理员信息为: {}", admin);

            if (adminDao.getAdminCountByAdminName(adminName) > 0) {
                log.error("管理员已存在");
                return ResponseEntity.ok(new Result(ResultEnum.ERROR.getStatus(), "管理员已存在", null));
            }

            if (adminDao.insertAnAdmin(admin)) {
                log.info("管理员注册成功");
                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("admin表插入记录失败");
        } catch (Exception e) {
            log.error("注册新管理员异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> deleteAdmin(String adminId, HttpSession session) {
        try {
            if (StringUtils.isEmpty(adminId)) {
                throw new NullPointerException("参数为空");
            }

            SessionInfo sessionInfo = (SessionInfo) session.getAttribute(StringConstant.SESSION_INFO);
            if (RoleConstant.ADMIN.equals(sessionInfo.getRole()) && adminId.equals(sessionInfo.getAdmin().getAdminId())) {
                if(adminDao.deleteAdminByAdminId(Integer.parseInt(adminId))) {
                    sessionService.logout(session);
                    return ResponseEntity.ok(ResultUtil.success());
                }
                throw new SQLException("admin表删除记录失败");
            }

            return ResponseEntity.ok(ResultUtil.error("必须由管理员本人执行注销操作"));
        } catch (Exception e) {
            log.error("注销管理员账号异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }
}
