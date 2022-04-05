package com.whl.messagesystem.commons.channel.management;

import com.whl.messagesystem.commons.channel.Channel;

/**
 * @author whl
 * @date 2022/2/28 18:08
 */
public class AdminListChannel implements Channel {

    private static String SCENE = "adminList";

    @Override
    public String getChannelName() {
        return SCENE;
    }
}
