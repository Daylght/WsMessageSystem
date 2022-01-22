package com.whl.messagesystem.service.admin;

import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.dao.AdminDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.entity.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public ResponseEntity<Result> getAdminList() {
        try {
            List<Admin> admins = adminDao.selectAllAdminsWithoutPassword();
            return ResponseEntity.ok(ResultUtil.success(admins));
        } catch (Exception e) {
            log.error("获取管理员列表异常: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

}
