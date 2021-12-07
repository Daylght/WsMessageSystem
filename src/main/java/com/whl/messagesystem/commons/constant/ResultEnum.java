package com.whl.messagesystem.commons.constant;

/**
 * @author whl
 * @date 2021/12/7 16:55
 */
public enum ResultEnum {
    /**
     * 状态码
     * 0----成功
     * 1----失败
     */
    SUCCESS(0,"成功"),
    ERROR(1,"失败");

    private final Integer status;
    private final String msg;

    ResultEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
