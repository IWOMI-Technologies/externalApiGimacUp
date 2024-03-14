/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.service;

import com.iwomi.External_api_cccNewUp.model.Nomenclature;
import com.iwomi.External_api_cccNewUp.repository.NomenclatureRepository;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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


    public Map<String, String> addListPaiement(Map<String,Object> payload) {
        Map<String, Object> res = new HashMap<>();
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel="http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(payload,headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/addPaiementGimac", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String,String> resp = new HashMap<>();
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

    public Map<String, String> billdetails(String etab, String cli, String cpt) {
        Map<String, Object> res= new HashMap<>();
        res.put("etab", etab);
        res.put("cli", cli);
        res.put("cpt", cpt);
        // response.put("tel", user.getTranstel);
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel="http://localhost:8084/";
        System.out.println("this is the phone number : "+ res);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res,headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getCli1", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String,String> resp = new HashMap<>();
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
    public String generateHash(String toHash) {
        System.out.println("this is the hash msg: "+toHash);
        System.out.println("this is the hash msg: "+toHash);
        System.out.println("this is the hash msg: "+toHash);
        // = "someRandomString";
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            hash = md.digest(toHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }
    private String convertToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}

