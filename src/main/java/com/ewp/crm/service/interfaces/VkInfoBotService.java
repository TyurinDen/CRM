package com.ewp.crm.service.interfaces;

import com.ewp.crm.service.impl.vkInfoBotClasses.messages.Message;

/**
 * Сервис, обеспечивающий работу VK-бота (VkInfoBot).
 *
 * Сервис ищет информацию о клиентах @see Client в БД CRM по командам от пользователя.
 * Под пользователем понимается пользователь VK, который отправляет сообщения-команды боту.
 * Сервис не работает с классом-сущностью Client напрямую, так так этот класс состоит из
 * огромного количества составных полей, большинство из которых не требуются пользователю.
 * Но сервис работает в таблицей client в БД CRM, обращаясь к некоторым ее полям.
 *
 * @author Tyurin Denis https://vk.com/dentttt
 */
public interface VkInfoBotService {
    /**
     * Возвращает confirmation token, необходимый для подключения Callback API VK к CRM.
     * @return confirmation token
     */
    String getConfirmationToken();

    /**
     * Отправляет найденных клиентов пользователю, приславшему сообщение боту.
     * @param message сообщение от пользователя VK. Сообщение является также и
     * исходящим, так как содержит необходимую информацию для корректной отправки
     * сообщения пользователю с результатами выполнения команды.
     */
    void sendResultMessage(Message message);

    /**
     * Отправляет пользователю сообщение со справкой по командам, которые поддерживает бот.
     * @param message сообщение от пользователя VK.
     */
    void sendHelpMessage(Message message);
}
