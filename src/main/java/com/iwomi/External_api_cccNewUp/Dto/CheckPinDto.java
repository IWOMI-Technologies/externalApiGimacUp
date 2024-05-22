package com.iwomi.External_api_cccNewUp.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckPinDto {
    private  String tel;
    private  String pin;
}
