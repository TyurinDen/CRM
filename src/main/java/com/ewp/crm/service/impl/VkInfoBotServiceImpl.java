package com.ewp.crm.service.impl;

import com.ewp.crm.configs.inteface.VkInfoBotConfig;
import com.ewp.crm.repository.interfaces.ClientRepositoryCustom;
import com.ewp.crm.service.impl.vkInfoBotClasses.commands.Command;
import com.ewp.crm.service.impl.vkInfoBotClasses.messages.Message;
import com.ewp.crm.service.interfaces.VkInfoBotService;
import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VkInfoBotServiceImpl implements VkInfoBotService {
    private static Logger logger = LoggerFactory.getLogger(VkInfoBotServiceImpl.class.getName());
    private final String VK_URL_API;
    private final String VK_API_VERSION;
    private final String VK_BOT_ACCESS_TOKEN;
    private final String VK_BOT_CONFIRMATION_TOKEN;
    private final String VK_BOT_CLUB_ID;
    private final OkHttpClient okHttpClient;
    private ClientRepositoryCustom clientCustomRepository;
    private Set<Command> commandSet;

    @Autowired
    public VkInfoBotServiceImpl(VkInfoBotConfig vkInfoBotConfig, @Qualifier("ClientRepositoryCustom") ClientRepositoryCustom clientCustomRepository) {
        String SQL_QUERY = "SELECT\n" +
                "    cl.client_id AS id,\n" +
                "    CONCAT(cl.last_name, ' ', cl.first_name) AS fio,\n" +
                "    CASE\n" +
                "        WHEN cl.phone_number IS NULL AND cl.email IS NULL THEN 'Контакты не указаны'" +
                "        WHEN cl.email IS NULL THEN CONCAT('Телефон: ', cl.phone_number)\n" +
                "        WHEN cl.phone_number IS NULL THEN CONCAT('Email: ', cl.email)\n" +
                "        ELSE CONCAT('Email: ', cl.email, ', телефон: ', cl.phone_number)\n" +
                "    END AS contacts,\n" +
                "    CONCAT(cl.country, ', ', cl.city) AS lives_in,\n" +
                "    cl.client_description_comment AS description,\n" +
                "    cl.comment,\n" +
                "    cl.postpone_comment AS p_comment,\n" +
                "    CONCAT(u.last_name, ' ', u.first_name) AS owner,\n" +
                "    CONCAT(u1.last_name, ' ', u1.first_name) AS mentor\n" +
                "FROM\n" +
                "    CLIENT cl\n" +
                "        LEFT JOIN\n" +
                "    user u ON cl.owner_user_id = u.user_id\n" +
                "        LEFT JOIN\n" +
                "    user u1 ON cl.owner_mentor_id = u1.user_id\n" +
                "WHERE ";
        Command findById = new Command("^i$|^и$|^ид$|^id$", 1,
                new String[]{"^[0-9]+\\*?$|^\\*[0-9]+$"}, SQL_QUERY + "cl.client_id RLIKE('%s') LIMIT %s");
        Command findByCityAndLastName = new Command("^cln$|^слн$", 2,
                new String[]{"^[a-zа-я]+\\*?$|^\\*[a-zа-я]+$", "^[a-zа-я]+\\*?$|^\\*[a-zа-я]+$"},
                SQL_QUERY + "cl.city RLIKE('%s') AND cl.last_name RLIKE('%s') LIMIT %s");
        Command findByCity = new Command("^c$|^city$|^с$|^сити$|^город$", 1,
                new String[]{"^[a-zа-я]+\\*?$|^\\*[a-zа-я]+$"}, SQL_QUERY + "cl.city RLIKE('%s') LIMIT %s");
        Command findByLastName = new Command("^ln$|^lastname$|^фамилия$|^лн$", 1,
                new String[]{"^[a-zа-я]+\\*?$|^\\*[a-zа-я]+$"}, SQL_QUERY + "cl.last_name RLIKE('%s') LIMIT %s");
        commandSet = new HashSet<>(Arrays.asList(findById, findByCityAndLastName, findByCity, findByLastName));

        this.clientCustomRepository = clientCustomRepository;
        okHttpClient = new OkHttpClient();
        VK_URL_API = vkInfoBotConfig.getVkApiUrl();
        VK_API_VERSION = vkInfoBotConfig.getVkApiVersion();
        VK_BOT_ACCESS_TOKEN = vkInfoBotConfig.getVkInfoBotAccessToken();
        VK_BOT_CONFIRMATION_TOKEN = vkInfoBotConfig.getVkInfoBotConfirmationToken();
        VK_BOT_CLUB_ID = vkInfoBotConfig.getVkInfoBotClubId();
    }

    @Override
    public String getConfirmationToken() {
        return VK_BOT_CONFIRMATION_TOKEN;
    }

    @Override
    public void sendResultMessage(Message message) {
        for (String client : packClientList(findClients(message.getText()))) {
            message.setText(client);
            sendMessage(message);
        }
    }

    private List<String> findClients(String messageText) {
        final String NOTHING_FOUND = "Ничего не найдено";
        final String INCORRECT_COMMAND = "Команда некорректна. См. справку: ? (help, помощь)";
        Command command = getCommand(messageText.trim().toLowerCase());
        if (command != null) {
            System.out.println(command.getSqlQuery());
            List<Object[]> clientList = clientCustomRepository.getClientsByCommandFromVkInfoBot(command.getSqlQuery());
            if (clientList.isEmpty()) {
                return Collections.singletonList(NOTHING_FOUND);
            }
            return formTextViewOfClientList(clientList);
        } else {
            return Collections.singletonList(INCORRECT_COMMAND);
        }
    }

    private List<String> packClientList(Iterable<String> clientsList) {
        // упаковывает клиентов в строку по MAX_RESULTS_PER_MESSAGE штук,
        // чтобы не отправлять каждого отдельным сообщением
        StringBuilder listItem = new StringBuilder();
        final int MAX_MESSAGE_LENGTH = 4096; // ограничение на длину сообщения ВК
        final int MAX_RESULTS_PER_MESSAGE = 4;
        int resultsPerMessage = 0;
        int messageLength = 0;
        List<String> packedClientList = new ArrayList<>();

        for (String client : clientsList) {
            if (messageLength + client.getBytes().length < MAX_MESSAGE_LENGTH &
                    resultsPerMessage < MAX_RESULTS_PER_MESSAGE) {
                listItem.append(client).append("\n");
                messageLength += client.getBytes().length;
                resultsPerMessage++;
            } else {
                packedClientList.add(listItem.toString());
                listItem = new StringBuilder();
                listItem.append(client).append("\n");
                messageLength = client.getBytes().length;
                resultsPerMessage = 1;
            }
        }
        packedClientList.add(listItem.toString());
        return packedClientList;
    }

    private List<String> formTextViewOfClientList(List<Object[]> clients) {
        //формирует читабельное представление результата native SQL запроса
        //необходим, так как native SQL запрос к БД возвращает List<Object[]>
        String[] prefixes = new String[]{"ID:", "ФИО:", "", "Живет:", "Описание:", "Комментарий:",
                "Отложенный комментарий:", "Менеджер:", "Ментор:"};
        int counter = 1;
        List<String> clientsList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Найдено: ").append(clients.size()).append("\n\n");
        for (Object[] objects : clients) {
            stringBuilder.append("> ").append(counter++).append(" из ").append(clients.size()).append("\n");
            for (int i = 0; i < prefixes.length; i++) {
                if (objects[i] != null) {
                    stringBuilder.append("> ").append(prefixes[i]).append(" ").append(objects[i]).append("\n");
                } else {
                    stringBuilder.append("> ").append(prefixes[i]).append(" ").append("N/A").append("\n");
                }
            }
            clientsList.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        return clientsList;
    }

    private Command getCommand(String messageText) {
        for (Command command : commandSet) {
            if (command.checkSyntax(messageText)) {
                return command;
            }
        }
        return null;
    }

    private void sendMessage(Message message) {
        RequestBody requestBody = new FormEncodingBuilder()
                .add("user_id", String.valueOf(message.getFromId()))
                .add("random_id", String.valueOf(message.getRandomId()))
                .add("group_id", String.valueOf(VK_BOT_CLUB_ID))
                .add("reply_to", String.valueOf(message.getId()))
                .add("message", String.valueOf(message.getText()))
                .add("access_token", VK_BOT_ACCESS_TOKEN)
                .add("v", VK_API_VERSION)
                .build();

        Request request = new Request.Builder().url(VK_URL_API + "/messages.send").post(requestBody).build();
        String responseBodyString = "No response body!";
        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                responseBodyString = responseBody.string();
                logger.info("Response from API VK: " + responseBodyString);
            }

            if (!response.isSuccessful()) {
                logger.error(String.format("Unable to send message! Server response: %s, response code: %d",
                        responseBodyString, response.code()));
            }
        } catch (Exception e) {
            logger.error("Unable to send message! ", e);
        }

    }

    @Override
    public void sendHelpMessage(Message message) {
        final String HELP_MESSAGE = "*************** VkInfoBot - поиск клиентов в CRM ***************\n\n" +
                " Бот понимает следующие команды (регистр игнорируется):\n" +
                "--------------------------------------------------------------------------------------------------\n" +
                "id [i, ид, и]: поиск по идентефикатору.\n" +
                "id 123: найти клиента с ID равным 123.\n" +
                "id *123: найти клиента с ID, заканчивающимся на 123.\n" +
                "id 123*: найти клиента с ID, начинающимся со 123.\n" +
                "--------------------------------------------------------------------------------------------------\n" +
                "cln [слн]: поиск по городу и фамилии.\n" +
                "cln Москва Иванов: найти клиентов из Москвы с фамилией\n" +
                "Иванов. Или cl м* и*: найти клиентов из города на \'м\'\n" +
                "с фамилией, начинающейся на \'и\'.\n" +
                "--------------------------------------------------------------------------------------------------\n" +
                "city [c, сити, город]: поиск по городу.\n" +
                "--------------------------------------------------------------------------------------------------\n" +
                "ln [lastname, фамилия, лн]: поиск по фамилии.\n" +
                "--------------------------------------------------------------------------------------------------\n" +
                "Все команды поддерживают символ \'*\' в аргументах.\n\n" +
                "Количество результатов ограничено двадцатью, но его\n" +
                "можно уменьшить, передав еще один аргумент команде:\n" +
                "id *123 5 - ограничить вывод пятью результатами.\n";
        message.setText(HELP_MESSAGE);
        sendMessage(message);
    }
}
