package com.whl.messagesystem.commons.config;

import com.whl.messagesystem.commons.interceptor.HandshakeInterceptorForWebSocket;
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
public class WebsocketConfiguration implements WebSocketConfigurer {

    @Resource
    private MessageServiceImpl messageServiceImpl;

    @Resource
    private HandshakeInterceptorForWebSocket handshakeInterceptorForWebSocket;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        //websocket地址 -> ws://localhost:8888/websocket?groupName=Xxx&adminId=Xxx
        registry.addHandler(messageServiceImpl,"/websocket")
                .addInterceptors(handshakeInterceptorForWebSocket)
                .setAllowedOrigins("*");
    }

}
