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
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String role;
}
