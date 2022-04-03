package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    List<Admin> selectAllAdmins();

    /**
     * 根据userId查询用户隶属的管理员
     * @param userId
     * @return
     */
    Admin selectAdminByUserId(int userId);

    /**
     * 在admin表中根据管理员id查找管理员信息
     * @param adminName
     * @return
     */
    Admin selectAdminByAdminName(String adminName);

    /**
     * 在admin表中根据管理员名查询对应的管理员数量
     * @param adminName
     * @return
     */
    int getAdminCountByAdminName(String adminName);

    /**
     * 在admin表中插入一条记录
     * @param admin
     * @return
     */
    boolean insertAnAdmin(Admin admin);

    /**
     * 在admin表中根据id删除一条记录
     * @param adminId
     * @return
     */
    boolean deleteAdminByAdminId(int adminId);

    /**
     * 在admin表中更新一条记录
     * @param admin
     * @return
     */
    boolean updateAdminNameAndPassword(@Param("admin") Admin admin);
}
