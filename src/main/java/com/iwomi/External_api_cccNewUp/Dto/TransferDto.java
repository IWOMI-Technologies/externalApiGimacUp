package com.iwomi.External_api_cccNewUp.Dto;

import com.iwomi.External_api_cccNewUp.Enum.LibEnum;
import com.iwomi.External_api_cccNewUp.Enum.RegionEnum;
import com.iwomi.External_api_cccNewUp.Enum.TranstypeEnum;
import com.iwomi.External_api_cccNewUp.Enum.TypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransferDto {
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
