package com.whl.messagesystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2021/12/29 15:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup {
    private String userId;
    private String groupId;
}
