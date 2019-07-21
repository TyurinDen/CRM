package com.ewp.crm.service.interfaces;

import com.ewp.crm.service.impl.vkInfoBotClasses.messages.Message;
import org.springframework.stereotype.Service;

@Service
public interface VkInfoBotService {

    String getConfirmationToken();

    void sendResultMessage(Message message);

    void sendHelpMessage(Message message);
}
