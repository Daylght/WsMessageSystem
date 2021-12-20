package com.whl.messagesystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2021/12/20 21:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    /**
     * userId: 自增主键，无意义
     * userName: 用户名
     * password: 密码
     * email: 电子邮箱
     */
    private String userId;
    private String userName;
    private String password;
    private String email;
}
