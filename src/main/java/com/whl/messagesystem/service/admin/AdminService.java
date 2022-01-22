package com.whl.messagesystem.service.admin;

import com.whl.messagesystem.model.Result;
import org.springframework.http.ResponseEntity;

/**
 * @author whl
 * @date 2022/1/22 11:25
 */
public interface AdminService {

    /**
     * 获取所有管理员的列表,只包含id和name，不包含password
     */
    ResponseEntity<Result> getAdminList();

}
