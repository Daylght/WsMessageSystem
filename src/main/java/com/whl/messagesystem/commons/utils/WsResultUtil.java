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

}
