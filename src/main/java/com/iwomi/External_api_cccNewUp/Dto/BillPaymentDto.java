package com.iwomi.External_api_cccNewUp.Dto;

import com.iwomi.External_api_cccNewUp.Enum.IntentEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BillPaymentDto {
    private IntentEnum intent;
    private String member;
    private String codewaldo;
    private String serviceRef;
    private String queryRef;
    private String contractRef;
}
