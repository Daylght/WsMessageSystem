package com.whl.messagesystem.commons.constant;

/**
 * @author whl
 * @date 2021/12/24 14:40
 */
public enum LoginResultConstant {
    WRONG_VERIFYCODE(0, "验证码错误");

    private final Integer code;
    private final String message;

    LoginResultConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
