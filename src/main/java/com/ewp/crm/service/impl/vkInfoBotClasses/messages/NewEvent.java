package com.ewp.crm.service.impl.vkInfoBotClasses.messages;

import lombok.Data;

@Data
public class NewEvent {
    private String type;

    private Message message;

    private long groupId;

}
