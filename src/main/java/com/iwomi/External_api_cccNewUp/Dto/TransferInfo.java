package com.iwomi.External_api_cccNewUp.Dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TransferInfo {
    public String fullName;
    public String fees;
    public double solde;
    public String reason;
    public boolean can;
}
