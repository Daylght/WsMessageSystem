package com.whl.messagesystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2021/12/7 15:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private Integer status;
    private String message;
    private T data;

}
