/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.service;

import com.iwomi.External_api_cccNewUp.model.responses;
import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import com.iwomi.External_api_cccNewUp.ussd.config.UssdConfigFile;
import com.iwomi.External_api_cccNewUp.ussd.utils.Factory;
import com.iwomi.External_api_cccNewUp.ussd.utils.UtilsUssd;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class UssdService {
   public ResponseEntity<?> startUSSDManager( String spid,  String timestamp,  String md5, String serviceid,  String endpoint,  String correlator,  String ussdCode) {
         Factory factory = new Factory();
        serviceid = "";
         String xmlStartUssdString = factory.generateStartUSSDNotification(spid, timestamp, md5, "", endpoint, correlator, ussdCode);
         String MessageUrl = UssdConfigFile.getStartUSSDManagerURL();
        System.out.println(MessageUrl);
        System.out.println(xmlStartUssdString);
        try {
             Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlStartUssdString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssd(), -1));
            }
            return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
        }
        catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssd(), -1));
        }
    }
}
