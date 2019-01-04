package com.test.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

@ApiModel(subTypes = {MainPayload.class,FraudPointPayload.class})
public abstract class Payload {

    @JsonIgnore
    public abstract String getPayloadName();
}
