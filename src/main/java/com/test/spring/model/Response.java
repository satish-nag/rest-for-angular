package com.test.spring.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
    Header header;
    Payload payload;
}
