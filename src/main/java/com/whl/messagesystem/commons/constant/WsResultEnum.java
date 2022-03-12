package com.whl.messagesystem.commons.constant;

/**
 * @author whl
 * @date 2022/2/2 21:02
 */
public enum WsResultEnum {

    CREATE_GROUP(1, "新增分组"),
    DELETE_GROUP(2, "删除分组"),
    JOIN_GROUP(3, "加入分组"),
    QUIT_GROUP(4, "退出分组"),
    KICK_MEMBER(5, "踢出分组"),
    DISSMISS_GROUP(6,"解散分组");

    private int type;
    private String message;

    WsResultEnum(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

}
