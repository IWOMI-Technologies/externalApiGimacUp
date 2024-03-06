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
 * @author TAGNE
 */
@Service
public class UssdFirstTrustService {

    @Autowired
    private NomenclatureRepository nomenclatureRepository;

    public JSONObject enpoint(JSONObject map) {
        System.out.println("Paiement frais debut :" + map);
        //String uri = serviceApiMaviance.getPaimentApiUrl() + "bicecPayFees";
        String uri = "http://svr-ussd.firsttrust.cm:9090/";
        Map<String, Object> response2 = new HashMap();
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

    public Map<String, String> getSolde( String cli, String cpt) {
        Map<String, String> res = new HashMap<>();
       // res.put("etab", etab);
        res.put("cli", cli);
        res.put("cpt", cpt);
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
        Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel + "/digitalbank/getSolde")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(res)
                .asString();
        Map<String, String> resp = new HashMap<>();
        System.out.println("body :" + response.getBody());
        if (response.getStatus() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("status", "01");
            resp.put("data", response.getBody());
        } else {
            resp.put("status", "100");
            resp.put("data", response.getBody());
        }
        return resp;
    }

    public Map<String, Object> getcptByTel(String telephone) {
        Map<String,String> res = new HashMap<>();
        res.put("telephone", telephone);
        //String baseUrel="http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
        Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel + "/digitalbank/getTelephone")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(res)
                .asString();
        Map<String,Object> resp = new HashMap<>();
        System.out.println("body :" + response.getBody());
        if (response.getStatus() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("status", "01");
            resp.put("data", response.getBody());
        } else {
            resp.put("status", "100");
            resp.put("data", response.getBody());
        }
        return resp;
    }

    public Map<String, String> addListPaiement(String etab, String cli, String cpt) {
        Map<String, Object> response = new HashMap<>();
        response.put("etab", etab);
        response.put("cli", cli);
        response.put("cpt", cpt);
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel="http://localhost:8084/";
        Unirest.config().verifySsl(false);
        HttpResponse<String> res = Unirest.post(baseUrel + "/digitalbank/requestPaiement")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(response)
                .asString();
        Map<String, String> resp = new HashMap<>();
        System.out.println("body :" + res.getBody());
        if (res.getStatus() == 200) {
            System.out.println("Token :" + res.getBody());
            resp.put("status", "01");
            resp.put("data", res.getBody());
        } else {
            resp.put("status", "100");
            resp.put("data", res.getBody());
        }
        return resp;
    }


    public Map<String, String> billdetails(String etab, String cli, String cpt) {
        Map<String, Object> response = new HashMap<>();
        response.put("etab", etab);
        response.put("cli", cli);
        response.put("cpt", cpt);
        // response.put("tel", user.getTranstel);
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel="http://localhost:8084/";
        Unirest.config().verifySsl(false);
        HttpResponse<String> res = Unirest.post(baseUrel + "/digitalbank/getBillToPay")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(response)
                .asString();
        Map<String, String> resp = new HashMap<>();
        System.out.println("body :" + res.getBody());
        if (res.getStatus() == 200) {
            System.out.println("Token :" + res.getBody());
            resp.put("status", "01");
            resp.put("data", res.getBody());
        } else {
            resp.put("status", "100");
            resp.put("data", res.getBody());
        }
        return resp;
    }

    public Map<String, String> CheckPin (String tel, String Pin) {
        Map<String, Object> response = new HashMap<>();
        response.put("tel", tel);
        response.put("Pin", Pin);
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel="http://localhost:8084/";
        Unirest.config().verifySsl(false);
        HttpResponse<String> res = Unirest.post(baseUrel + "/digitalbank/checkPin")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(response)
                .asString();
        Map<String, String> resp = new HashMap<>();
        System.out.println("body :" + res.getBody());
        if (res.getStatus() == 200) {
            System.out.println("Token :" + res.getBody());
            resp.put("status", "01");
            resp.put("data", res.getBody());
        } else {
            resp.put("status", "100");
            resp.put("data", res.getBody());
        }
        return resp;
    }
}

