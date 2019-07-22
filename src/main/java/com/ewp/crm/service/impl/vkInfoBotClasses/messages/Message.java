package com.ewp.crm.service.impl.vkInfoBotClasses.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс, описывающий объект новое сообщение (message_new) от VK API
 *
 * См. документацию VK API: https://vk.com/dev/objects/message
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private long date;

    private long fromId;

    private int id;

    private int out;

    private long peerId;

    private String text;

    private int convMessagesId;

    private List fwdMessagesList;

    private boolean important;

    private int randomId;

    // Заглушка, так как бот с вложениями не работает, только с текстом сообщения
    private List attachmentsList;

    private boolean isHidden;

}
