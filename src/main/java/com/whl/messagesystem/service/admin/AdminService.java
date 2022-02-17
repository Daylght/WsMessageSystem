package com.whl.messagesystem.service.admin;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.AdminRegisterDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2022/1/22 11:25
 */
public interface AdminService {

    /**
     * 获取所有管理员的列表,只包含id和name，不包含password
     */
    ResponseEntity<Result> getAdminList();

    /**
     * 注册新管理员
     * @param adminRegisterDTO
     * @return
     */
    ResponseEntity<Result> register(AdminRegisterDTO adminRegisterDTO);

    /**
     * 注销管理员账号
     * @param adminId
     * @param session
     * @return
     */
    ResponseEntity<Result> deleteAdmin(String adminId, HttpSession session);
}
