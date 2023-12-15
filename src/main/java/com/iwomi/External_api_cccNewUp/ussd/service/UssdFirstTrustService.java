/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.service;

import com.iwomi.External_api_cccNewUp.model.Nomenclature;
import com.iwomi.External_api_cccNewUp.repository.NomenclatureRepository;
import java.util.HashMap;
import java.util.Map;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author TAGNE
 */
@Service
public class UssdFirstTrustService {
    
    @Autowired
    private NomenclatureRepository nomenclatureRepository;
    
        public JSONObject enpoint(JSONObject map) { 
        System.out.println("Paiement frais debut :" + map);
        //String uri = serviceApiMaviance.getPaimentApiUrl() + "bicecPayFees";
         String uri =  "http://svr-ussd.firsttrust.cm:9090/";
        Map< String, Object> response2 = new HashMap();
        System.out.println("yvo le map1:" + map);
       JSONObject res1 = new JSONObject();

         response2 = map.toMap();
        System.out.println("yvo l'url:" + uri);
        System.out.println("yvo le map1:" + response2);
        Unirest.config().verifySsl(false);
        HttpResponse<String> responsepay = Unirest.post(uri)
               // .header("Authorization", "Bearer "+token)
                .header("Content-Type", "application/json")
                .body(response2)
                .asString();
        System.out.println("This is the result pay fees " + responsepay.toString());
        System.out.println("This is the status " + responsepay.getStatus());
     switch (responsepay.getStatus()) {
         case 200:
             JSONObject res = new JSONObject(responsepay.getBody());
             return res;
         case 401:
             System.out.println("token error:unauthorized");
             return null; //System.out.prinln("token error:"+response.getBody())unauthorized";
         case 500:
             System.out.println("token error:" + responsepay.getBody());
             return null; //"internal server error";
         default:
             String result = responsepay.getBody();
             System.out.println("token error:" + responsepay.getBody());
             return null; //result;
     }
    
   }
    
   
}
