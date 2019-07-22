package com.ewp.crm.configs.inteface;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface VkInfoBotConfig {

    String getVkInfoBotClubId();
    String getVkApiUrl();
    String getVkApiVersion();
    String getVkInfoBotAccessToken();
    String getVkInfoBotConfirmationToken();

}
