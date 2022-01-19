package com.whl.messagesystem.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2021/12/7 15:38
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    @ApiModelProperty("0  成功；1  失败")
    private Integer status;
    @ApiModelProperty("提示信息")
    private String message;
    @ApiModelProperty("业务数据")
    private T data;

}
