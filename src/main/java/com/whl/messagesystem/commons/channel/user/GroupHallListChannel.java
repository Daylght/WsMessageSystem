package com.whl.messagesystem.commons.channel.user;

import com.whl.messagesystem.commons.channel.Channel;

/**
 * @author whl
 * @date 2022/2/28 10:50
 */
public class GroupHallListChannel implements Channel {

    private static final String scene = "groupHallList";

    private String adminId = null;

    public GroupHallListChannel(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String getChannelName() {
        StringBuilder name = new StringBuilder(scene);
        return name.append("#").append(adminId).toString();
    }
}
