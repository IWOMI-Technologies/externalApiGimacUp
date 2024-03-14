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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res,headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getSolde", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String,String> resp = new HashMap<>();
        JSONObject res2F = new JSONObject(response.getBody());
        JSONObject valueSolde =cptClientSolde(res2F);
        System.out.println("body :" + response.getBody());
        System.out.println("body 2:" +  valueSolde.get("sold").toString());
        System.out.println("body 3:" +  valueSolde.get("name").toString());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("status", "01");
            resp.put("solde", valueSolde.get("sold").toString());
            resp.put("name", valueSolde.get("name").toString());
        } else {
            resp.put("status", "100");
            resp.put("data", response.getBody());
        }
        return resp;
    }

    public JSONObject cptClientSolde(JSONObject t) {
        JSONArray response1 = new JSONArray();
        JSONObject response = new JSONObject();
        response.put("sold",t.get("current_balance"));
        response.put("name",t.get("inti"));
    return response;

    }
    public Map<String, String> getSolde2( String cli, String cpt) {
        Map<String, String> res = new HashMap<>();
        // res.put("etab", etab);
        res.put("cli", cli);
        res.put("cpt", cpt);
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res,headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getSolde", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String,String> resp = new HashMap<>();
        JSONObject jsd =new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("status", "01");
            resp.put("data", response.getBody());
        } else {
            resp.put("status", "100");
            resp.put("data", response.getBody());
        }
        return resp;
    }


    public JSONObject getcptByTel(String telephone) {
        Map<String,String> res = new HashMap<>();
        res.put("telephone", telephone);
        //String baseUrel="http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
       /* Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel+"/digitalbank/getTelephone")
                .header("Content-Type", "application/json")
                .body(res)
                .asString();*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res,headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getTelephone", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String,Object> resp = new HashMap<>();
        JSONObject jsd =new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if(response.getStatusCodeValue() == 200) {
            System.out.println("Token :" + response.getBody());
            jsd.put("status", "01");
            jsd.put("data", response.getBody());
           // ResponseEntity<JSONObject> respon = response.getBody();

        } else {
            jsd.put("status", "100");
            jsd.put("data", response.getBody());
        }
        return jsd;
    }

    public Map<String,Object> getCpt1(Map<String,String> res) {
        //String baseUrel="http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
       /* Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel+"/digitalbank/getTelephone")
                .header("Content-Type", "application/json")
                .body(res)
                .asString();*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res,headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getCpt1", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String,Object> resp = new HashMap<>();
        JSONObject jsd =new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if(response.getStatusCodeValue() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("status", "01");
            resp.put("cpt", response.getBody());
            // ResponseEntity<JSONObject> respon = response.getBody();

        } else {
            resp.put("status", "100");
            resp.put("data", response.getBody());
        }
        return resp;
    }

    public Map<String,Object> getCli(Map<String,String> res) {
        //String baseUrel="http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
       /* Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel+"/digitalbank/getTelephone")
                .header("Content-Type", "application/json")
                .body(res)
                .asString();*/
        System.out.println("this is the phone number : "+ res);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res,headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getCli1", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String,Object> resp = new HashMap<>();
        JSONObject jsd =new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if(response.getStatusCodeValue() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("status", "01");
            resp.put("cli", response.getBody());
            // ResponseEntity<JSONObject> respon = response.getBody();
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
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel="http://localhost:8084/";
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
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel="http://localhost:8084/";
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

    public Map<String, Object> CheckPin (String tel, String pin) {
        Map<String, Object> res = new HashMap<>();
        res.put("tel", tel);
        res.put("pin", pin);
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel="http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res,headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/checkPin", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String,Object> resp = new HashMap<>();
        JSONObject jsd =new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if(response.getStatusCodeValue() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("message", response.getBody());
            resp.put("success", "01");
            // ResponseEntity<JSONObject> respon = response.getBody();
        } else {
            resp.put("status", "100");
            resp.put("data", response.getBody());
        }
        return resp;
    }
}

