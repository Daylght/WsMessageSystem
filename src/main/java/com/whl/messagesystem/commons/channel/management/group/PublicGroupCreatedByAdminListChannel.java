package com.whl.messagesystem.commons.channel.management.group;

import com.whl.messagesystem.commons.channel.Channel;

import java.net.InetAddress;
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
        String localHostAddress = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            localHostAddress = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "获取链接地址失败";
        }

        return "ws://" + localHostAddress + ":8888" + "/websocket?groupName=NoGroup&adminId=" + adminId + "&scene=" + SCENE;
    }
}
