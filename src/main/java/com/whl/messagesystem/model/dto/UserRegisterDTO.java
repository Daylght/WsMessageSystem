package com.whl.messagesystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2022/1/16 22:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    private String userName;
    private String password;
    private String adminId;
}
