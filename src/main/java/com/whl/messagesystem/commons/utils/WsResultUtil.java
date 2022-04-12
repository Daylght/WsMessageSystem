package com.whl.messagesystem.commons.utils;

import com.whl.messagesystem.commons.constant.WsResultEnum;
import com.whl.messagesystem.model.WebsocketResult;

/**
 * @author whl
 * @date 2022/2/2 21:07
 */
public class WsResultUtil {

    public static WebsocketResult<Object> createGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.CREATE_GROUP.getType());
        websocketResult.setMessage(WsResultEnum.CREATE_GROUP.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> createPublicGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.CREATE_PUBLIC_GROUP.getType());
        websocketResult.setMessage(WsResultEnum.CREATE_PUBLIC_GROUP.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> dismissPublicGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.DISMISS_PUBLIC_GROUP.getType());
        websocketResult.setMessage(WsResultEnum.DISMISS_PUBLIC_GROUP.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> deleteGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.DELETE_GROUP.getType());
        websocketResult.setMessage(WsResultEnum.DELETE_GROUP.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> dismissGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.DISMISS_GROUP.getType());
        websocketResult.setMessage(WsResultEnum.DISMISS_GROUP.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> joinGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.JOIN_GROUP.getType());
        websocketResult.setMessage(WsResultEnum.JOIN_GROUP.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> quitGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.QUIT_GROUP.getType());
        websocketResult.setMessage(WsResultEnum.QUIT_GROUP.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> kickMember(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.KICK_MEMBER.getType());
        websocketResult.setMessage(WsResultEnum.KICK_MEMBER.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> giveUpManageGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.GIVE_UP_MANAGE_GROUP.getType());
        websocketResult.setMessage(WsResultEnum.GIVE_UP_MANAGE_GROUP.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> choiceManagePrivateGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.CHOICE_GROUP_MANAGE.getType());
        websocketResult.setMessage(WsResultEnum.CHOICE_GROUP_MANAGE.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> giveUpManageUser(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.GIVE_UP_MANAGE_USER.getType());
        websocketResult.setMessage(WsResultEnum.GIVE_UP_MANAGE_USER.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> choiceManageUser(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.CHOICE_USER_MANAGE.getType());
        websocketResult.setMessage(WsResultEnum.CHOICE_USER_MANAGE.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> choiceAdmin(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.CHOICE_ADMIN.getType());
        websocketResult.setMessage(WsResultEnum.CHOICE_ADMIN.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> logicDeleteUser(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.LOGIC_DELETE_USER.getType());
        websocketResult.setMessage(WsResultEnum.LOGIC_DELETE_USER.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> recoverUser(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.RECOVER_USER.getType());
        websocketResult.setMessage(WsResultEnum.RECOVER_USER.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> completeDeleteUser(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.COMPLETE_DELETE_USER.getType());
        websocketResult.setMessage(WsResultEnum.COMPLETE_DELETE_USER.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> registerAdmin(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.REGISTER_ADMIN.getType());
        websocketResult.setMessage(WsResultEnum.REGISTER_ADMIN.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> registerUser(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.REGISTER_USER.getType());
        websocketResult.setMessage(WsResultEnum.REGISTER_USER.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> deleteAdmin(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.DELETE_ADMIN.getType());
        websocketResult.setMessage(WsResultEnum.DELETE_ADMIN.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> updateAdminNameAndPassword(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.UPDATE_ADMIN_NAME_AND_PASSWORD.getType());
        websocketResult.setMessage(WsResultEnum.UPDATE_ADMIN_NAME_AND_PASSWORD.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

    public static WebsocketResult<Object> updateUserNameAndPassword(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.UPDATE_USER_NAME_AND_PASSWORD.getType());
        websocketResult.setMessage(WsResultEnum.UPDATE_USER_NAME_AND_PASSWORD.getMessage());
        websocketResult.setData(data);
        return websocketResult;
    }

}
