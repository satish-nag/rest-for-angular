package com.test.spring.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ResponseSerializer extends JsonSerializer<Response> {

    @Override
    public void serialize(Response response, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        Payload payload = response.getPayload();
        jsonGenerator.writeObjectField("Header",response.getHeader());
        jsonGenerator.writeObjectField(payload.getPayloadName(),payload);
        jsonGenerator.writeEndObject();
    }
}
