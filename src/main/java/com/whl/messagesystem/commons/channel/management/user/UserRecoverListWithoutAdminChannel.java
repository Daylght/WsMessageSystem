package com.whl.messagesystem.commons.channel.management.user;

import com.whl.messagesystem.commons.channel.Channel;

/**
 * @author whl
 * @date 2022/2/28 18:12
 */
public class UserRecoverListWithoutAdminChannel implements Channel {

    private static String SCENE = "userRecoverListWithoutAdmin";

    @Override
    public String getChannelName() {
        return SCENE;
    }
}
