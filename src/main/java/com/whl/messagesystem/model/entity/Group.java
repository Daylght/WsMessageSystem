package com.whl.messagesystem.model.entity;

import lombok.Data;

/**
 * @author whl
 * @date 2021/12/21 21:50
 */
@Data
public class Group {
    private String groupId;
    private String groupName;
    private int maxCount;
    private String adminId;
    private String creatorId;
    private boolean adminCreated;
}
