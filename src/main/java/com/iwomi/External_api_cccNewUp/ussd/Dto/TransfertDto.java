package com.iwomi.External_api_cccNewUp.ussd.Dto;

import com.iwomi.External_api_cccNewUp.ussd.Enum.LibEnum;
import com.iwomi.External_api_cccNewUp.ussd.Enum.RegionEnum;
import com.iwomi.External_api_cccNewUp.ussd.Enum.TranstypeEnum;
import com.iwomi.External_api_cccNewUp.ussd.Enum.TypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

@Getter
@Setter
@Builder
public class TransfertDto {
    private String etab;
    private TypeEnum type;
    private RegionEnum region;
    private String nat;
    private String cli;
    private String mtrans;
    private LibEnum lib;
    private String typeco;
    private String top;
    private String tel;
    private String telop;
    private String pin;
    private String codewaldo;
    private String codewalop;
    private TranstypeEnum transtype;
    private String member;

}
