package com.whl.messagesystem.commons.channel.management.user;

import com.whl.messagesystem.commons.channel.Channel;

/**
 * @author whl
 * @date 2022/2/28 18:13
 */
public class UserWithAdminListChannel implements Channel {

    private static final String SCENE = "userWithAdminList";

    private String adminId = null;

    public UserWithAdminListChannel(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String getChannelName() {
        return SCENE + "#" + adminId;
    }
}
