package com.test.spring.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class Response {
    @ApiModelProperty(reference = "com.test.spring.model.Header")
    Header header;

    @ApiModelProperty(reference = "com.test.spring.model.Payload")
    Payload payload;
}
