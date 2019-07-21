package com.ewp.crm.configs;

import com.ewp.crm.configs.inteface.VkInfoBotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "file:./vk-info-bot.properties", encoding = "UTF-8")
public class VkInfoBotConfigImpl implements VkInfoBotConfig {
    private String vkInfoBotClubId;
    private String vkVkInfoBotAccessToken;
    private String vkVkInfoBotConfirmationToken;
    private String vkApiUrl;
    private String vkApiVersion;
    private static Logger logger = LoggerFactory.getLogger(VkInfoBotConfigImpl.class.getName());

    @Autowired
    public VkInfoBotConfigImpl(Environment env) {
        try {
            this.vkInfoBotClubId = env.getProperty("vk.info-bot.id");
            logger.info("vkInfoBotClubId: {}", vkInfoBotClubId);
            this.vkVkInfoBotAccessToken = env.getProperty("vk.info-bot.access_token");
            logger.info("vkVkInfoBotAccessToken: {}", vkVkInfoBotAccessToken);
            this.vkVkInfoBotConfirmationToken = env.getProperty("vk.info-bot.confirmation_token");
            logger.info("vkVkInfoBotConfirmationToken: {}", vkVkInfoBotConfirmationToken);
            this.vkApiUrl = env.getProperty("vk.apiUrl");
            this.vkApiVersion = env.getProperty("vk.api.version");
        } catch (IllegalStateException e) {
            logger.error("Vk InfoBot is not initialized. Check vk-info-bot.properties file", e);
        }
    }

    @Override
    public String getVkInfoBotClubId() {
        return vkInfoBotClubId;
    }

    @Override
    public String getVkInfoBotAccessToken() {
        return vkVkInfoBotAccessToken;
    }

    @Override
    public String getVkInfoBotConfirmationToken() {
        return vkVkInfoBotConfirmationToken;
    }

    @Override
    public String getVkApiUrl() {
        return vkApiUrl;
    }

    @Override
    public String getVkApiVersion() {
        return vkApiVersion;
    }

}
