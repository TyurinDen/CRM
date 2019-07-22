package com.ewp.crm.service.impl.vkInfoBotClasses.messages;

/**
 * Класс, описывающий новое событие от VK API
 *
 * См. документацию VK API: https://vk.com/dev/groups_events
 *
 * @author Tyurin Denis https://vk.com/dentttt
 */
public class NewEvent {
    private String type;

    private Message message;

    private long groupId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
