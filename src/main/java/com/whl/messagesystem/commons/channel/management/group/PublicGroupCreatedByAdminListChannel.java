package com.whl.messagesystem.commons.channel.management.group;

import com.whl.messagesystem.commons.channel.Channel;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * @author whl
 * @date 2022/3/13 16:14
 */
public class PublicGroupCreatedByAdminListChannel implements Channel {

    private static final String SCENE = "publicGroupCreatedByAdminList";

    private String adminId = null;

    public PublicGroupCreatedByAdminListChannel(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String getChannelName() {
        return SCENE + "#" + adminId;
    }

    /**
     * 返回当前频道的websocket链接地址
     *
     * @return
     */
    public String getChannelLink() {
        // fixme:这里不应该写死，应该写成动态获取本机ip和端口的形式
        return "ws://47.108.139.22:8888/websocket?groupName=NoGroup&adminId=" + adminId + "&scene=" + SCENE;
    }
}
