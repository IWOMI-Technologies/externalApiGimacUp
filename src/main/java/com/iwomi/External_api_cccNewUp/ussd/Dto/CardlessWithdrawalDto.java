package com.iwomi.External_api_cccNewUp.ussd.Dto;

import com.iwomi.External_api_cccNewUp.ussd.Enum.IntentEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardlessWithdrawalDto {
    private IntentEnum intent;
    private String validitytime;
    private String member;
    private String rmobile;
    private String smobile;
    private String amount;
    private String ref;
    private String currency;
    private String createtime;
}
