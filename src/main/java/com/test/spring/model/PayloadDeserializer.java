package com.test.spring.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@JsonComponent
public class PayloadDeserializer extends JsonDeserializer<Payload>{

    @Autowired
    Map<String,Payload> payloads;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Payload deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode treeNode = jsonParser.readValueAsTree();
        AtomicReference<Payload> payload = new AtomicReference<>();
        if(treeNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) treeNode;
            objectNode.fields().forEachRemaining(stringJsonNodeEntry -> {
                try {
                    if (payloads.get(stringJsonNodeEntry.getKey()) != null) {
                        Payload payloadType = payloads.get(stringJsonNodeEntry.getKey());
                        payload.set(objectMapper.readValue(stringJsonNodeEntry.getValue().toString(), payloadType.getClass()));
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return payload.get();
    }
}
