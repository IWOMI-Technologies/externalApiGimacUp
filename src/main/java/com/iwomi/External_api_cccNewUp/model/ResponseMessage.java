package com.iwomi.External_api_cccNewUp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class ResponseMessage {
    private  String message;
    private  String command;
}
