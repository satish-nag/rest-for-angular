package com.test.spring.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@JsonComponent
public class ResponseDeserializer extends JsonDeserializer<Response>{

    @Autowired
    Map<String,Payload> payloads;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Response deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Response response = new Response();
        TreeNode treeNode = jsonParser.readValueAsTree();
        if(treeNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) treeNode;
            objectNode.fields().forEachRemaining(stringJsonNodeEntry -> {
                try {
                    if ("header".equalsIgnoreCase(stringJsonNodeEntry.getKey())) {
                        response.setHeader(objectMapper.readValue(stringJsonNodeEntry.getValue().toString(), Header.class));
                    }
                    else if(payloads.get(stringJsonNodeEntry.getKey())!=null){
                        Payload payloadType = payloads.get(stringJsonNodeEntry.getKey());
                        Payload payload = objectMapper.readValue(stringJsonNodeEntry.getValue().toString(), payloadType.getClass());
                        response.setPayload(payload);
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
        return response;
    }
}
