package com.ewp.crm.service.impl.vkInfoBotClasses.messages;

import lombok.Data;

/**
 * Класс, описывающий новое событие от VK API
 *
 * См. документацию VK API: https://vk.com/dev/groups_events
 *
 * @author Tyurin Denis https://vk.com/dentttt
 */
@Data
public class NewEvent {
    private String type;

    private Message message;

    private long groupId;

}
