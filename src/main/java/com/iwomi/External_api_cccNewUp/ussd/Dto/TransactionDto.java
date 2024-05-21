package com.iwomi.External_api_cccNewUp.ussd.Dto;

import com.iwomi.External_api_cccNewUp.model.SocketTextHandler;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TransactionDto {
    private String telephone;
    private String pin;
    private String member;
    private String codewalop;
    private String amount;
    private String nat;
    private String region;

}
