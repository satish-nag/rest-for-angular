package com.test.spring.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("payload")
@ApiModel
public class MainPayload extends Payload {

    @ApiModelProperty
    Response fraudPointResponse;

    @Override
    public String getPayloadName() {
        return "payload";
    }
}
