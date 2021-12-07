package com.whl.messagesystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2021/12/7 17:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    /**
     * userId: 自增主键，无意义
     * userName: 用户名
     * password: 密码
     * email: 电子邮箱
     * role: 0表示用户，1表示管理员
     */
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String role;
}
