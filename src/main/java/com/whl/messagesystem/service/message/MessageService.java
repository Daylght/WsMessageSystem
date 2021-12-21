package com.whl.messagesystem.service.message;

import org.springframework.web.socket.WebSocketMessage;

import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2021/12/21 20:03
 */
public interface MessageService {
    /**
     * 在指定的分组内广播消息
     * @param groupName
     * @param message
     */
    void publish(String groupName, WebSocketMessage<?> message);


    /**
     * 删除指定分组的WebSocket连接
     * @param groupName
     */
    void deleteGroup(String groupName, HttpSession session);
}
