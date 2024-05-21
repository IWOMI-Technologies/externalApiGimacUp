package com.iwomi.External_api_cccNewUp.ussd.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckpinDto {
    private  String tel;
    private  String pin;
}
