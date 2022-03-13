package com.whl.messagesystem.commons.channel.user;

import com.whl.messagesystem.commons.channel.Channel;

/**
 * @author whl
 * @date 2022/2/28 10:50
 */
public class GroupHallListChannel implements Channel {

    private static final String SCENE = "groupHallList";

    private String adminId = null;

    public GroupHallListChannel(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String getChannelName() {
        return SCENE + "#" + adminId;
    }
}
