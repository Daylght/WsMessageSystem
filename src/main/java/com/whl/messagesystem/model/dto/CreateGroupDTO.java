package com.whl.messagesystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2022/1/19 20:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupDTO {
    private String groupName;
    private Integer maxCount;
    private String adminId;
    private String creatorId;
}
