package com.whl.messagesystem.service.message;

import org.springframework.web.socket.WebSocketMessage;

import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2022/1/26 17:10
 */
public interface MessageService {

    void publish(String groupName, WebSocketMessage<?> message);

    void deleteGroup(String groupName, HttpSession session);

}
