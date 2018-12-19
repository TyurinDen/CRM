package com.ewp.crm.service.conversation;

import com.ewp.crm.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JMConversationHelperImpl implements JMConversationHelper {

    private final int CHAT_MESSAGE_LIMIT = 40;

    private final List<JMConversation> conversations;

    @Autowired
    public JMConversationHelperImpl(List<JMConversation> conversations) {
        this.conversations = conversations;
    }

    @Override
    public void endChat(Client client) {
        for (JMConversation conversation: conversations) {
            conversation.endChat(client);
        }
    }

    @Override
    public ChatMessage sendMessage(ChatMessage message) {
        for (JMConversation conversation: conversations) {
            if (message.getChatType() == conversation.getChatTypeOfConversation()) {
               return conversation.sendMessage(message);
            }
        }
        return message;
    }

    @Override
    public List<ChatMessage> getNewMessages(Client client) {
        List<ChatMessage> list = new LinkedList<>();
        for (JMConversation conversation: conversations) {
            List<ChatMessage> conversationMsg = conversation.getNewMessages(client, CHAT_MESSAGE_LIMIT);
            list.addAll(0, conversationMsg);
        }
        list.sort(Comparator.comparing(ChatMessage::getTime));
        return list;
    }

    @Override
    public List<ChatMessage> getMessages(Client client) {
        List<ChatMessage> list = new LinkedList<>();
        for (JMConversation conversation: conversations) {
            List<ChatMessage> conversationMsg = conversation.getMessages(client, CHAT_MESSAGE_LIMIT);
            list.addAll(0, conversationMsg);
        }
        list.sort(Comparator.comparing(ChatMessage::getTime));
        return list;
    }

    @Override
    public Map<ChatType, String> getReadMessages(Client client) {
        Map<ChatType, String> chatTypeStringMap = new HashMap<>();
        for (JMConversation conversation: conversations) {
            String lastMsg = conversation.getReadMessages(client);
            chatTypeStringMap.put(conversation.getChatTypeOfConversation(), lastMsg);
        }
        return chatTypeStringMap;
    }

    @Override
    public List<Interlocutor> getInterlocutors(Client client) {
        List<Interlocutor> list = new LinkedList<>();
        for (JMConversation conversation: conversations) {
            list.add(conversation.getInterlocutor(client));
        }
        return list;
    }

    @Override
    public List<Interlocutor> getUs() {
        List<Interlocutor> list = new LinkedList<>();
        for (JMConversation conversation: conversations) {
            list.add(conversation.getMe());
        }
        return list;
    }

}
