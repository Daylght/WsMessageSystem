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

    public static WebsocketResult<Object> deleteGroup(Object data) {
        WebsocketResult<Object> websocketResult = new WebsocketResult<>();
        websocketResult.setType(WsResultEnum.DELETE_GROUP.getType());
        websocketResult.setMessage(WsResultEnum.DELETE_GROUP.getMessage());
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

}
