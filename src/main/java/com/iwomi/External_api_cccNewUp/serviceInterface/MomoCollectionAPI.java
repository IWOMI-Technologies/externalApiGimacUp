/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.serviceInterface;

import java.util.Map;

/**
 *
 * @author user
 */
public interface MomoCollectionAPI {
    public String getToken( String apiUser, String apiKey, String subKey);
    
    public Map<String, String> performRedrawal(String amount,String number, String payerMsg, String payeeMsg, String currency,String token, String subKey,String s);

    public String OperationStatus(String referenced_id, String subKey, String token);
    
    public String getAccountBalance(String subKey,String token);
    
    public String MomoAccountExist(String accountHolderId,String subKey, String token);
    
}

