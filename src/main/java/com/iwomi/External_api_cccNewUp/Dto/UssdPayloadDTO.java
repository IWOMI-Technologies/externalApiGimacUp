package com.iwomi.External_api_cccNewUp.Dto;

@lombok.Data
public class UssdPayloadDTO {
    public String msisdn;
    public String sessionid;
    public String message;
    public String provider;
}
