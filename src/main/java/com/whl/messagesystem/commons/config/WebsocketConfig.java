package com.whl.messagesystem.commons.config;

import com.whl.messagesystem.commons.interceptor.HandshakeInterceptorForWebSocket;
import com.whl.messagesystem.service.message.MessageService;
import com.whl.messagesystem.service.message.MessageServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @author whl
 * @date 2021/12/21 20:57
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Resource
    private MessageServiceImpl messageService;

    @Resource
    private HandshakeInterceptorForWebSocket handshakeInterceptorForWebSocket;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //websocket地址 -> ws://localhost:8888/commonWebSocket?groupName=Xxx
        //注册比赛的websocket
        registry.addHandler(messageService, "/commonWebsocket")
                .addInterceptors(handshakeInterceptorForWebSocket)
                .setAllowedOrigins("*");
    }

}
