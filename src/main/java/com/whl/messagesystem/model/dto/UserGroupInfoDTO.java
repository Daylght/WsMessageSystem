package com.whl.messagesystem.model.dto;

import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2022/3/27 14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupInfoDTO {
    User user;
    Group group;
}
