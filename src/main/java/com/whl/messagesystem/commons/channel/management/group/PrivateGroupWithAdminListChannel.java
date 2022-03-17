package com.whl.messagesystem.commons.channel.management.group;

import com.whl.messagesystem.commons.channel.Channel;

/**
 * @author whl
 * @date 2022/2/28 18:23
 */
public class PrivateGroupWithAdminListChannel implements Channel {

    private static final String SCENE = "privateGroupWithAdminList";

    private String adminId = null;

    public PrivateGroupWithAdminListChannel(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String getChannelName() {
        return SCENE + "#" + adminId;
    }
}
