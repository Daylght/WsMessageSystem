package com.whl.messagesystem.model.vo;

import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2022/1/22 18:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupVo {
    private String groupId;
    private String groupName;
    private int maxCount;
    private String adminId;
    private String adminName;
    private String creatorId;
    private String creatorName;
}
