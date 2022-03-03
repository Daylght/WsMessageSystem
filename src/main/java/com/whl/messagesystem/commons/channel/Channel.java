package com.whl.messagesystem.commons.channel;

/**
 * @author whl
 * @date 2022/2/28 10:42
 */
public interface Channel {

    /**
     * 获取当前频道的名字，该名字用于websocket消息的分组存储
     * @return
     */
    String getChannelName();

}
