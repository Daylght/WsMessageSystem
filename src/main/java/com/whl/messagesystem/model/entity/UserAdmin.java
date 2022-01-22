package com.whl.messagesystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2022/1/22 12:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdmin {
    private String userId;
    private String adminId;
}
