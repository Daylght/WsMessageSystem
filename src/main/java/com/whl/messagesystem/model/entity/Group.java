package com.whl.messagesystem.model.entity;

import lombok.Data;

/**
 * @author whl
 * @date 2021/12/21 21:50
 */
@Data
public class Group {
    /**
     * groupName: 组名
     * adminId: 该组所属的管理员名称
     * memberCount: 该组的成员数量
     */
    private String groupName;
    private String adminId;
    private Integer memberCount;
}
