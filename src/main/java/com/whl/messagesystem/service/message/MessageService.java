package com.whl.messagesystem.service.message;

import org.springframework.web.socket.WebSocketMessage;

import javax.servlet.http.HttpSession;

/**
 * @author whl
 * @date 2022/1/26 17:10
 */
public interface MessageService {

    /**
     * 在指定的频道中广播消息
     * @param channelName
     * @param message
     */
    void publish(String channelName, WebSocketMessage<?> message);

    /**
     * 删除指定的频道，并关闭此频道中的所有连接
     * @param channelName
     */
    void deleteChannel(String channelName);

}
