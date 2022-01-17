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
     * 2----用户已存在
     */
    SUCCESS(0, "成功"),
    ERROR(1, "失败"),
    USER_EXIST(2, "用户已存在");

    private final int status;
    private final String msg;

    ResultEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
