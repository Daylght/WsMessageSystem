package com.whl.messagesystem.model.entity;

import lombok.*;

/**
 * @author whl
 * @date 2022/2/27 17:48
 */
@Data
public class PublicGroup {
    private String groupId;
    private String groupName;
    private int maxCount;
    private boolean adminCreated;
    private String adminId;
}
