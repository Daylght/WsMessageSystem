package com.whl.messagesystem.model.dto;

import com.whl.messagesystem.model.entity.Admin;
import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2022/1/21 16:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionInfo {
    private User user;
    private Group group;
    private Admin admin;
}
