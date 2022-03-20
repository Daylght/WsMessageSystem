package com.whl.messagesystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2021/12/29 15:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private String adminId;
    private String adminName;
    private String password;
}
