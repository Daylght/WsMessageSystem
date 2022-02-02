package com.whl.messagesystem.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author whl
 * @date 2022/2/2 21:08
 */
@Data
@ToString
public class WebsocketResult<T> {

    @ApiModelProperty("提示信息")
    private String message;

    @ApiModelProperty("1 创建分组; 2 删除分组")
    private Integer type;

    @ApiModelProperty("业务数据")
    private T data;
}
