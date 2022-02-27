package com.whl.messagesystem.service.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static com.whl.messagesystem.commons.constant.StringConstant.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author whl
 * @date 2022/1/26 17:10
 */
@Slf4j
@Component
public class WebsocketEndPoint extends TextWebSocketHandler implements MessageService {

    /**
     * pubsubGroupName -> List<WebSocketSession> <br>
     * pubsubGroupName 是专门用于广播与订阅的组名，和小组名不同 <br>
     * 在组内广播时，pubsubGroupName 和 groupName 是一致的 <br>
     * 在大厅广播时，pubsubGroupName 为 "NoGroup" 和 adminId 拼接后的字符串
     */
    protected final Map<String, CopyOnWriteArrayList<WebSocketSession>> webSocketSessionsMap = new ConcurrentHashMap<>();


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.info("afterConnectionClosed");
        //获取链接
        String pubsubGroupName = getPubsubGroupName(session);
        webSocketSessionsMap.get(pubsubGroupName).remove(session);
        log.info("从{}小组中移除当前用户的websocket会话", pubsubGroupName);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        String pubsubGroupName = getPubsubGroupName(session);

        if (webSocketSessionsMap.get(pubsubGroupName) == null) {
            log.warn("{}小组的websocket尚未初始化，现在进行初始化", pubsubGroupName);
            //多个比赛的集合初始化是同步的
            synchronized (this) {
                if (webSocketSessionsMap.get(pubsubGroupName) == null) {
                    List<WebSocketSession> webSocketSessionList = new CopyOnWriteArrayList<>();
                    webSocketSessionList.add(session);
                    webSocketSessionsMap.put(pubsubGroupName, (CopyOnWriteArrayList<WebSocketSession>) webSocketSessionList);
                }
            }
        } else {
            //对集合的操作也是同步的
            log.info("{}小组的websocket已被初始化，把当前用户的会话加入该小组的websocket", pubsubGroupName);
            synchronized (webSocketSessionsMap.get(pubsubGroupName)) {
                webSocketSessionsMap.get(pubsubGroupName).add(session);
            }
        }
        log.info("afterConnectionEstablished ------ 连接已建立");

    }


    @Override
    public void publish(String channel, WebSocketMessage<?> message) {
        //判断这个比赛是否存在
        if (webSocketSessionsMap.get(channel) != null) {
            //这里表示在比赛广播消息的时候是没办法同时增加会话进来的
            synchronized (webSocketSessionsMap.get(channel)) {
                final Iterator<WebSocketSession> iterator = webSocketSessionsMap.get(channel).iterator();
                //迭代发送消息
                while (iterator.hasNext()) {
                    final WebSocketSession webSocketSession = iterator.next();
                    //判断该会话是否还是开启的
                    if (webSocketSession.isOpen()) {
                        try {
                            //向该回话发送消息
                            webSocketSession.sendMessage(message);
                        } catch (IOException e) {
                            log.warn(channel + "有一个websocket会话连接断开" + e.getMessage());
                            //移除这个出现异常的会话
                            iterator.remove();
                        }
                    } else {
                        //移除这个已经关闭的会话
                        iterator.remove();
                        log.warn(channel + "移除一个已经断开了连接的websocket");
                    }
                }
            }
        }
    }

    @Override
    public void deleteGroup(String channel, HttpSession session) {
        List<WebSocketSession> webSocketSessionList = webSocketSessionsMap.get(channel);
        for (WebSocketSession webSocketSession : webSocketSessionList) {
            try {
                if (webSocketSession.isOpen()) {
                    webSocketSession.close();
                    log.info("WebSocketSessionId={}已断开", webSocketSession.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据 WebSocketSession 中的 groupName 和 adminId 生成用于广播订阅用的 pubsubGroupName
     *
     * @param session
     * @return
     */
    private String getPubsubGroupName(WebSocketSession session) {
        String pubsubGroupName;
        final String groupName = (String) session.getAttributes().get("groupName");
        final String adminId = (String) session.getAttributes().get("adminId");

        if (NO_GROUP.equals(groupName)) {
            pubsubGroupName = groupName + adminId;
        } else {
            pubsubGroupName = groupName;
        }

        return pubsubGroupName;
    }


}

