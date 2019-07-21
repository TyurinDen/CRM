package com.ewp.crm.service.impl.vkInfoBotClasses.messages;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Collections;

public class CustomNewEventDeserializer extends StdDeserializer<NewEvent> {

    public CustomNewEventDeserializer() {
        this(null);
    }

    public CustomNewEventDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public NewEvent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        NewEvent newEvent = new NewEvent();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode jsonRootNode = codec.readTree(jsonParser);

        newEvent.setType(jsonRootNode.get("type").asText());
        newEvent.setGroupId(jsonRootNode.get("group_id").asLong());

        JsonNode objectNode = jsonRootNode.get("object");
        Message message = Message.builder()
                .date(objectNode.get("date").asLong())
                .fromId(objectNode.get("from_id").asLong())
                .id(objectNode.get("id").asInt())
                .out(objectNode.get("out").asInt())
                .peerId(objectNode.get("peer_id").asLong())
                .text(objectNode.get("text").asText())
                .convMessagesId(objectNode.get("conversation_message_id").asInt())
                .fwdMessagesList(Collections.singletonList(objectNode.get("fwd_messages").asText()))
                .important(objectNode.get("important").asBoolean())
                .randomId(objectNode.get("random_id").asInt())
                .attachmentsList(Collections.singletonList(objectNode.get("fwd_messages").asText()))
                .isHidden(objectNode.get("is_hidden").asBoolean()).build();
        newEvent.setMessage(message);
        return newEvent;
    }
}
