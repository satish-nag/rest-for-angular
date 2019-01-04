package com.test.spring.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

//@JsonComponent
public class PayloadSerializer extends JsonSerializer<Payload>{

    @Override
    public void serialize(Payload payload, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObject(payload);
        //jsonGenerator.writeObjectField(payload.getPayloadName(),payload);
        jsonGenerator.writeEndObject();
    }
}
