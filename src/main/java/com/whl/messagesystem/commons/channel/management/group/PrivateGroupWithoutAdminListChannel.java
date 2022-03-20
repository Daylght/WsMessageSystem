package com.whl.messagesystem.commons.channel.management.group;

import com.whl.messagesystem.commons.channel.Channel;

/**
 * @author whl
 * @date 2022/2/28 18:25
 */
public class PrivateGroupWithoutAdminListChannel implements Channel {

    private static final String SCENE = "privateGroupWithoutAdminList";

    @Override
    public String getChannelName() {
        return SCENE;
    }
}
