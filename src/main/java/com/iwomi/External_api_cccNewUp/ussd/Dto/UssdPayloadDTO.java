package com.iwomi.External_api_cccNewUp.ussd.Dto;

@lombok.Data
public class UssdPayloadDTO {
    private String msisdn;
    private String sessionid;
    private String message;
    private String provider;
}
