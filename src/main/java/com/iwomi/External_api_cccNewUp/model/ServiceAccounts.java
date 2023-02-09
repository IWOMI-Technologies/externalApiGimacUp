/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAccounts {
    private String name;
    private String pwd;
    private String descp;
    private String code;
    private Date crdt;
    private Date mdt;
    private String chl1;
    private String chl2;
    private String chl3;
    private String chl4;
    private String chl5;
    
}
