package com.whl.messagesystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2021/12/7 18:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String userName;
    private String password;
    private String serverCode;
}
