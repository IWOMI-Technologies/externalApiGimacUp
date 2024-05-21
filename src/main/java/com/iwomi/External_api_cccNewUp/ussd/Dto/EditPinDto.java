package com.iwomi.External_api_cccNewUp.ussd.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class EditPinDto {
    private String tel;
    private String oldpin;
    private String newpin;
}
