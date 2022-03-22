package com.whl.messagesystem.commons.channel.management.user;

import com.whl.messagesystem.commons.channel.Channel;

/**
 * @author whl
 * @date 2022/2/28 18:13
 */
public class UserWithoutAdminListChannel implements Channel {

    private static final String SCENE = "userWithoutAdminList";

    @Override
    public String getChannelName() {
        return SCENE;
    }
}
