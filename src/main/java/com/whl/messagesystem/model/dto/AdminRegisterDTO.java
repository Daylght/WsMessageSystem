package com.whl.messagesystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2022/2/17 13:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterDTO {
    private String adminName;
    private String password;
}
