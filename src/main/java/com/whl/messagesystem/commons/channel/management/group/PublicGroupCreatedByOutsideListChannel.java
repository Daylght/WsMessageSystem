package com.whl.messagesystem.commons.channel.management.group;

import com.whl.messagesystem.commons.channel.Channel;

/**
 * @author whl
 * @date 2022/2/28 18:17
 */
public class PublicGroupCreatedByOutsideListChannel implements Channel {

    private static final String SCENE = "publicGroupCreatedByOutsideList";

    @Override
    public String getChannelName() {
        return SCENE;
    }
}
