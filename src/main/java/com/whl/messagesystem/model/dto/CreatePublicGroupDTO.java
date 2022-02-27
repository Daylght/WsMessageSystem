package com.whl.messagesystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2022/2/27 17:08
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePublicGroupDTO {
    private String groupName;
    private Integer maxCount;
    private String adminId;
}
