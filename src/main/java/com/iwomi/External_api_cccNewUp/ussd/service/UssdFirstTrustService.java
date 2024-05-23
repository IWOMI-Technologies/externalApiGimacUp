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
import java.util.*;
import java.util.stream.Collectors;

import com.iwomi.External_api_cccNewUp.ussd.Core.GlobalResponse;
import com.iwomi.External_api_cccNewUp.ussd.Dto.EditPinDto;
import com.iwomi.External_api_cccNewUp.ussd.Dto.TransactionDto;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.aspectj.weaver.ast.Var;
import org.hibernate.mapping.Array;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
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
    public Map<String, String> getSolde(String cli, String cpt) {
        Map<String, String> res = new HashMap<>();
        // res.put("etab", etab);
        res.put("cli", cli);
        res.put("cpt", cpt);
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getSolde", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, String> resp = new HashMap<>();
        JSONObject res2F = new JSONObject(response.getBody());
        JSONObject valueSolde = cptClientSolde(res2F);
        System.out.println("body :" + response.getBody());
        System.out.println("body 2:" + valueSolde.get("sold").toString());
        System.out.println("body 3:" + valueSolde.get("name").toString());
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
        response.put("sold", t.get("current_balance"));
        response.put("name", t.get("inti"));
        return response;
    }

    public Map<String, String> nomendata(Map<String, String> payload) {
        Map<String, String> res = new HashMap<>();
        // res.put("etab", etab);
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(payload , headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getSolde", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, String> resp = new HashMap<>();
        JSONObject res2F = new JSONObject(response.getBody());
        JSONObject valueSolde = cptClientSolde(res2F);
        System.out.println("body :" + response.getBody());
        System.out.println("body 2:" + valueSolde.get("sold").toString());
        System.out.println("body 3:" + valueSolde.get("name").toString());
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

    public Map<String, Object> walletinq(Map<String, Object> payload) {
        System.out.println("fabrication objet" + payload);
        Map<String, String> res = new HashMap<>();
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getwalletInquiry", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, Object> resp = new HashMap<>();
        JSONObject jsd = new JSONObject();
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
        Map<String, String> res = new HashMap<>();
        res.put("telephone", telephone);
        String baseUrel="http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
       /* Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel+"/digitalbank/getTelephone")
                .header("Content-Type", "application/json")
                .body(res)
                .asString();*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getTelephone", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, Object> resp = new HashMap<>();
        JSONObject jsd = new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
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

    public Map<String, Object> getCpt1(Map<String, String> res) {
        String baseUrel="http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
       /* Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel+"/digitalbank/getTelephone")
                .header("Content-Type", "application/json")
                .body(res)
                .asString();*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getCpt1", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, Object> resp = new HashMap<>();
        JSONObject jsd = new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
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

    public Map<String, Object> getCli(Map<String, String> res) {
        String baseUrel="http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
       /* Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel+"/digitalbank/getTelephone")
                .header("Content-Type", "application/json")
                .body(res)
                .asString();*/
        System.out.println("this is the phone number : " + res);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getCli1", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String, Object> resp = new HashMap<>();
        JSONObject jsd = new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("cli", response.getBody().toString());
            // ResponseEntity<JSONObject> respon = response.getBody();
        } else {
            resp.put("status", "100");
            resp.put("data", response.getBody());
        }
        return resp;
    }


    public Map<String, Object> addListPaiement(TransactionDto payload) {
        Map<String, Object> res = new HashMap<>();
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/addPaiementGimac", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String, Object> resp = new HashMap<>();
        JSONObject jsd = new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
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

    public Map<String, Object> billdetails(Map<String, Object> payload) {
        Map<String, Object> res = new HashMap<>();
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
        System.out.println("this is the phone number : " + res);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getBillToPay", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String, Object> resp = new HashMap<>();
        JSONObject jsd = new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
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

    public Map<String, Object> billpayment(Map<String, Object> payload) {
        Map<String, Object> res = new HashMap<>();
        // response.put("tel", user.getTranstel);
        String baseUrel = "http://192.168.30.58:8087/";
        //String baseUrel = "http://localhost:8087/";
        System.out.println("this is the phone number : " + res);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/v1/gimacSend", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String, Object> resp = new HashMap<>();
        JSONObject jsd = new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
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

    public Map<String, Object> CheckPin(String tel, String pin) {
        Map<String, Object> res = new HashMap<>();
        res.put("tel", tel);
        res.put("pin", pin);
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/checkPin", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String, Object> resp = new HashMap<>();
        JSONObject jsd = new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
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

   /* public JSONObject  makeOperation(Map<String, String> payload){
        JSONObject response3 = new JSONObject();
       String path ="http://57.128.163.118:8084/digitalbank/getNomencTabcdAcscd";
        try {
            Unirest.config().verifySsl(false);
            // HttpResponse<String> response = Unirest.post(url)
            HttpResponse<String> response = Unirest.post(path)
                    //.header("access_token",  path)
                    // .header("path",  path)
                    .header("Content-Type", "application/json")
                    .body(payload)
                    .asString();
            System.out.println("This is the status " + response.getStatusText());
            System.out.println("This response: " + response.getBody());
            if (response.getStatus() == 200) {
                System.out.println(response.getBody());
                JSONObject jsonObject = new JSONObject(response.getBody());
               response3.put("data",jsonObject);
                response3.put("status", "01");
               // response3.put("data", response.getBody());
            } else{
                response3.put("status", "100");
                response3.put("data", response.getBody());
            }
        } catch (HttpStatusCodeException ex) {
            System.out.println("Response body is " + ex.getResponseBodyAsString());
            response3.put("status", "501");
            response3.put("data", ex.getResponseBodyAsString());

        }
        return response3;
    }*/
    public JSONObject nomitems (JSONObject t) {
            JSONObject response = new JSONObject();
            response.put("nat", t.get("data"));
            return response;
        }

    public Map<String,Object>airtimereload(Map<String,Object>payload){
        Map<String, Object> res = new HashMap<>();
        // response.put("tel", user.getTranstel);
        String baseUrel = "http://192.168.30.58:8087/";
        //String baseUrel = "http://localhost:8087/";
        System.out.println("this is the phone number : " + res);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/v1/gimacSend", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String, Object> resp = new HashMap<>();
        JSONObject jsd = new JSONObject();
        System.out.println("body :" + response.getBody());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
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
    public Map<String, String> walletinquiryftsl (String cli, String cpt) {
        Map<String, String> res = new HashMap<>();
        // res.put("etab", etab);
        res.put("cli", cli);
        res.put("cpt", cpt);
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getSolde", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, String> resp = new HashMap<>();
        JSONObject res2F = new JSONObject(response.getBody());
        JSONObject valueSolde = cptClientSolde2(res2F);
        System.out.println("body :" + response.getBody());
        //System.out.println("body 2:" + valueSolde.get("sold").toString());
        System.out.println("body 3:" + valueSolde.get("name").toString());
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("status", "01");
            resp.put("name", valueSolde.get("name").toString());
        } else {
            resp.put("status", "100");
            resp.put("data", response.getBody());
        }
        return resp;
    }

    public JSONObject cptClientSolde2(JSONObject t) {
        JSONObject response = new JSONObject();
        response.put("name", t.get("inti"));
        return response;
    }

    public Map<String, Object>  editpin(EditPinDto payload){
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrel + "/digitalbank/changePinU", entity, Map.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, Object> resp = new HashMap<>();
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("::::::::::::::: response.getBody() :::::::::::::::" + response.getBody());
            System.out.println("::::::::::::::: response.getBody() Class :::::::::::::::" + response.getBody().getClass());
            return GlobalResponse.responseBuilder3("::: successful :::","01", response.getBody());
        } else {
            System.out.println("::::::::::::::: Request failed with status code ::::::::::: " + response.getStatusCodeValue());
            return GlobalResponse.responseBuilder3("::: oldpin incorrect :::", "100", null);
        }
    }

    public String generateHash(String toHash) {
        System.out.println("this is the hash msg: " + toHash);
        System.out.println("this is the hash msg: " + toHash);
        System.out.println("this is the hash msg: " + toHash);
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
    public ArrayList<Nomenclature>  getNomencTabcdAcscd() {
       // String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ArrayList> response = restTemplate.getForEntity(baseUrel + "/digitalbank/getNomenDataByTabcdussd22", ArrayList.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("::::::::::::::: response.getBody() :::::::::::::::" + response.getBody());
            System.out.println("::::::::::::::: response.getBody() Class :::::::::::::::" + response.getBody().getClass());
            return GlobalResponse.responseBuilder4("::: successful :::","01", response.getBody());
        } else {
            System.out.println("::::::::::::::: Request failed with status code ::::::::::: " + response.getStatusCodeValue());
            return GlobalResponse.responseBuilder4("::: unsuccessful :::","100", null);
        }
    }

    public JSONArray trans(JSONArray data) {
        JSONArray response1 = new JSONArray();
        for (int i = 0; i < data.length(); i++) {
            JSONObject ls = data.getJSONObject(i); // Access JSONObject by index
            JSONObject res1 = new JSONObject();
            res1.put("crtd", ls.get("data")); // Put JSONObject directly
            response1.put(res1);
        }
        return response1;
    }

/* if (entity.getStatusCode() == HttpStatus.OK) {
        System.out.println("::::::::::::::: response.getBody() :::::::::::::::" + response.getBody());
        System.out.println("::::::::::::::: response.getBody() Class :::::::::::::::" + entity.getBody().getClass());
        return GlobalResponse.responseBuilder("::: successful :::", HttpStatus.CREATED,"01", entity.getBody());
    } else {
        System.out.println("::::::::::::::: Request failed with status code ::::::::::: " + response.getStatusCodeValue());
        return GlobalResponse.responseBuilder("::: unsuccessful :::", HttpStatus.BAD_REQUEST,"100", null);
    }*/
}

