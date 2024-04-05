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

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.aspectj.weaver.ast.Var;
import org.hibernate.mapping.Array;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
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
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
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

    public Map<String, String> getSolde2(String cli, String cpt) {
        Map<String, String> res = new HashMap<>();
        // res.put("etab", etab);
        res.put("cli", cli);
        res.put("cpt", cpt);
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(res, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/getSolde", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, String> resp = new HashMap<>();
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
        //String baseUrel="http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
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
        //String baseUrel="http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
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
        //String baseUrel="http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
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


    public Map<String, String> addListPaiement(Map<String, Object> payload) {
        Map<String, Object> res = new HashMap<>();
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/digitalbank/addPaiementGimac", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        Map<String, String> resp = new HashMap<>();
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
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
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
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
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
        //String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
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

    /*public JSONObject nomitems (JSONObject T){
        String data = T.get("data").toString();
        JSONArray ls = new JSONArray(data);
        JSONArray vam = new JSONArray();
        JSONObject redo = new JSONObject();
        for (int a = 0; a < ls.length(); a++){
            JSONObject var = ls.getJSONObject(a);
            String accesscode = var.getString("accesscode");
            String mainservice = var.getString("mainservice");
            String nat = var.getString("nat");
            String top = var.getString("top");
            String member = var.getString("member");

            JSONObject ace = new JSONObject();
            // ace.put("accesscode", accesscode);
            ace.put("mainservice", mainservice);
            ace.put("nat", nat);
            ace.put("top", top);
            ace.put("member", member);
            vam.put(ace);
        }
        redo.put("data",vam);
        return redo;
    }*/

/*   public  ArrayList nomitems(ArrayList t) {
        ArrayList newData = new ArrayList<>();
        ArrayList data = t.get(1);
        data.forEach(m -> {
            if (Objects.equals(m.get("nat"), "TRIN")) {
                m.remove("mainservice");
                newData.add(m);
            }
        });
        return newData;
    }*/

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
    public Map<String, Object> getNomencTabcdAcscd(String tabcd,String acscd,String etab) {
        Map<String, Object> response= new HashMap<>();
        Nomenclature t = nomenclatureRepository.findUrl2(tabcd,acscd,"0", etab);
        response.put("success", "01");
        response.put("message", "liste des infos");
        response.put("data", t);
        return response;
    }
}

