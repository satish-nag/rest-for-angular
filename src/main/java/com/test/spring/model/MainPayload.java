package com.test.spring.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("payload")
public class MainPayload extends Payload {
    Response fraudPointResponse;

    @Override
    public String getPayloadName() {
        return "payload";
    }
}
