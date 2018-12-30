package com.test.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component("fraudPointPayload")
public class FraudPointPayload extends Payload {

    @JsonIgnore
    public static final String PAYLOAD_NAME = "fraudPointPayload";

    public Map<String,String> props = new HashMap<>();

    @Override
    public String getPayloadName() {
        return PAYLOAD_NAME;
    }
}
