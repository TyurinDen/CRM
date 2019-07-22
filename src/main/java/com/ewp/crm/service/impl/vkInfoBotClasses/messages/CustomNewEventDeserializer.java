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
        Message message = new Message();
        message.setDate(objectNode.get("date").asLong());
        message.setFromId(objectNode.get("from_id").asLong());
        message.setId(objectNode.get("id").asInt());
        message.setOut(objectNode.get("out").asInt());
        message.setPeerId(objectNode.get("peer_id").asLong());
        message.setText(objectNode.get("text").asText());
        message.setConvMessagesId(objectNode.get("conversation_message_id").asInt());
        message.setFwdMessagesList(Collections.singletonList(objectNode.get("fwd_messages").asText()));
        message.setImportant(objectNode.get("important").asBoolean());
        message.setRandomId(objectNode.get("random_id").asInt());
        message.setAttachmentsList(Collections.singletonList(objectNode.get("attachments").asText()));
        message.setIsHidden(objectNode.get("is_hidden").asBoolean());

        newEvent.setMessage(message);
        return newEvent;
    }
}
