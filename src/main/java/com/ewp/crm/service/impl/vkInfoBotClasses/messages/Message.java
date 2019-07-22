package com.ewp.crm.service.impl.vkInfoBotClasses.messages;

import java.util.List;

/**
 * Класс, описывающий объект новое сообщение (message_new) от VK API
 *
 * См. документацию VK API: https://vk.com/dev/objects/message
 */
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public long getPeerId() {
        return peerId;
    }

    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getConvMessagesId() {
        return convMessagesId;
    }

    public void setConvMessagesId(int convMessagesId) {
        this.convMessagesId = convMessagesId;
    }

    public List getFwdMessagesList() {
        return fwdMessagesList;
    }

    public void setFwdMessagesList(List fwdMessagesList) {
        this.fwdMessagesList = fwdMessagesList;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public int getRandomId() {
        return randomId;
    }

    public void setRandomId(int randomId) {
        this.randomId = randomId;
    }

    public List getAttachmentsList() {
        return attachmentsList;
    }

    public void setAttachmentsList(List attachmentsList) {
        this.attachmentsList = attachmentsList;
    }

    public boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean hidden) {
        isHidden = hidden;
    }
}
