package com.whl.messagesystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2021/12/7 17:05
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    /**
     * todo:插入一个管理员账户作为后台管理系统的登录账号
     * userId: 自增主键，无意义
     * userName: 用户名
     * password: 密码
     * email: 电子邮箱
     * role: 0表示用户，1表示管理员
     * showStatus: 0表示该用户没被删除，1表示该用户已被删除
     */
    private String userId;
    private String userName;
    private String password;
    private int showStatus;

}
