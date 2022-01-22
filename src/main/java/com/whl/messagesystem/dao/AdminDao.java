package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author whl
 * @date 2022/1/22 11:32
 */
@Mapper
public interface AdminDao {

    /**
     * 查找所有的管理员id与name，以list形式返回
     * @return
     */
    List<Admin> selectAllAdminsWithoutPassword();

    /**
     * 根据userId查询用户隶属的管理员
     * @param userId
     * @return
     */
    Admin selectAdminByUserId(int userId);
}
