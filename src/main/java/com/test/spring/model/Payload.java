package com.test.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Payload {

    @JsonIgnore
    public abstract String getPayloadName();
}
