package com.whl.messagesystem.service.admin;

import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.AdminInfo;
import com.whl.messagesystem.model.dto.AdminRegisterDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2022/1/22 11:25
 */
public interface AdminService {

    /**
     * 获取所有管理员的列表
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

    /**
     * 更新管理员的用户名与密码
     * @param adminInfo
     * @param session
     * @return
     */
    ResponseEntity<Result> updateAdminNameAndPassword(AdminInfo adminInfo, HttpSession session);

    /**
     * 用户选择一个管理员作为自己的管理员
     * @param adminId
     * @param session
     * @return
     */
    ResponseEntity<Result> choiceAnAdmin(String adminId, HttpSession session);
}
