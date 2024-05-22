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
import com.iwomi.External_api_cccNewUp.ussd.Enum.LibEnum;
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
        HttpEntity<Map> entity = new HttpEntity<Map>(payload, headers);
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
        Map<String, Object> Obj = walfunc(payload);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(Obj, headers);
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
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
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

    public Map<String, Object> getCpt1(Map<String, Object> res) {
        String baseUrel = "http://192.168.30.59:8084/";
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

    public Map<String, Object> getCli(Map<String, Object> res) {
        String baseUrel = "http://192.168.30.59:8084/";
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


    public Map<String, Object> addListPaiement(Map<String, Object> payload) {
        Map<String, Object> res = new HashMap<>();
        String baseUrel = "http://192.168.30.59:8084/";
        //String baseUrel = "http://localhost:8084/";
        Map<String, Object> obj = addlistfunction(payload);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(obj, headers);
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

    public JSONObject nomitems(JSONObject t) {
        JSONObject response = new JSONObject();
        response.put("nat", t.get("data"));
        return response;
    }

    public Map<String, Object> airtimereload(Map<String, Object> payload) {
        String baseUrel = "http://192.168.30.58:8087/";
        //String baseUrel = "http://localhost:8087/";
        Map<String, Object> Obj = airtimefunc(payload);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(Obj, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrel + "/v1/gimacSend", entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, Object> resp = new HashMap<>();
        response.getStatusCode();
        if (response.getStatusCodeValue() == 200) {
            System.out.println("Token :" + response.getBody());
            resp.put("status", "01");
            resp.put("cli", response.getBody());
        } else {
            resp.put("status", "100");
            resp.put("data", response.getBody());
        }
        return resp;
    }

    public Map<String, String> walletinquiryftsl(String cli, String cpt) {
        Map<String, String> res = new HashMap<>();
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

    public Map<String, Object> requesttopay(Map<String, Object> payload) {
        //String baseUrel = "http://192.168.30.58:8087/";
        String baseUrel = "http://localhost:8084/";
        Map<String, Object> obj1 = request_to_pay(payload);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(obj1, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrel + "/v1/gimacSend", entity, Map.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, Object> resp = new HashMap<>();
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("::::::::::::::: response.getBody() :::::::::::::::" + response.getBody());
            System.out.println("::::::::::::::: response.getBody() Class :::::::::::::::" + response.getBody().getClass());
            return GlobalResponse.responseBuilder3("::: successful :::", "01", response.getBody());
        } else {
            System.out.println("::::::::::::::: Request failed with status code ::::::::::: " + response.getStatusCodeValue());
            return GlobalResponse.responseBuilder3("::: oldpin incorrect :::", "100", null);
        }
    }
    public Map<String, Object> cardless_with (Map<String, Object> payload) {
        //String baseUrel = "http://192.168.30.58:8087/";
        String baseUrel = "http://localhost:8084/";
        Map<String, Object> obj1 = cardless(payload);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(obj1, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrel + "/v1/gimacSend", entity, Map.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        Map<String, Object> resp = new HashMap<>();
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("::::::::::::::: response.getBody() :::::::::::::::" + response.getBody());
            System.out.println("::::::::::::::: response.getBody() Class :::::::::::::::" + response.getBody().getClass());
            return GlobalResponse.responseBuilder3("::: successful :::", "01", response.getBody());
        } else {
            System.out.println("::::::::::::::: Request failed with status code ::::::::::: " + response.getStatusCodeValue());
            return GlobalResponse.responseBuilder3("::: oldpin incorrect :::", "100", null);
        }
    }


    public Map<String, Object> addlistfunction(Map<String, Object> payload) {
        System.out.println(":::::::::: fabrication object payment ::::::::" + payload);
        Map<String, Object> obj1 = getCli(payload);
        Map<String, Object> response = new HashMap<>();
        response.put("etab", "001");
        response.put("type", "firstrust");
        response.put("region", payload.get("region"));
        response.put("nat", payload.get("nat"));
        response.put("cli", obj1.get("cli"));
        response.put("mtrans", payload.get("amount"));
        response.put("lib", LibEnum.WALLET_TO_WALLET);
        response.put("typeco", "VIRE");
        response.put("network", "");
        response.put("top", "VIRE");
        response.put("tel", payload.get("telephone"));
        response.put("telop", "");
        response.put("pin", payload.get("pin"));
        response.put("codewaldo", payload.get("telephone"));
        response.put("codewalop", payload.get("codewalop"));
        response.put("partnerid", "");
        response.put("partnerlib", "");
        response.put("member", payload.get("member"));
        response.put("transtype", "RE");
        response.put("recievercustmerdata", "");
        response.put("vouchercode", "");
        return response;
    }

    public Map<String, Object> billfunc(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("intent", "bill_inquiry");
        response.put("member", payload.get("member"));
        response.put("codewaldo", payload.get("codewaldo"));
        response.put("serviceRef", "SRV_001");
        response.put("queryRef", "CTR_REF");
        response.put("contractRef", payload.get("contractRef"));
        System.out.println(":::::::: fabrication de facturier :::::::::" + payload.get("contractRef"));
        return response;
    }

    public Map<String, Object> airtimefunc(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("intent", "mobile_reload");
        response.put("member", payload.get("member"));
        response.put("rmobile", payload.get("codewaldo"));
        response.put("smobile", payload.get("tel"));
        response.put("amount", payload.get("amount"));
        response.put("ref", payload.get("contractRef"));
        System.out.println(":::::::: fabrication de facturier :::::::::" + payload.get("contractRef"));
        return response;
    }

    public Map<String, Object> walfunc(Map<String, Object> payload) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("intent", "account_inquiry");
        resp.put("member", payload.get("member"));
        resp.put("codewalop", payload.get("codewalop"));
        return resp;
    }

    public Map<String, Object> request_to_pay(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("intent", "account_inquiry");
        response.put("ref", payload.get("ref"));
        response.put("wdes", payload.get("codewalop"));
        response.put("wsou", payload.get("tel"));
        response.put("amount", payload.get("amount"));
        response.put("member", payload.get("member"));
        response.put("currency", "950");
        response.put("purchaseref", "ctr1235");
        response.put("createtime", "");
        return response;
    }
    public Map<String, Object> cardless (Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("intent", "cardless_withdrawal");
        response.put("ref", payload.get("ref"));
        response.put("rmobile", payload.get("codewalop"));
        response.put("smobile", payload.get("tel"));
        response.put("amount", payload.get("amount"));
        response.put("member", payload.get("member"));
        response.put("currency", "950");
        response.put("validitytime", "");
        response.put("createtime", "");
        return response;
    }

    public Map<String, Object> editpin(EditPinDto payload) {
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
            return GlobalResponse.responseBuilder3("::: successful :::", "01", response.getBody());
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

    public ArrayList<Nomenclature> getNomencTabcdAcscd() {
        // String baseUrel = "http://192.168.30.59:8084/";
        String baseUrel = "http://localhost:8084/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ArrayList> response = restTemplate.getForEntity(baseUrel + "/digitalbank/getNomenDataByTabcdussd22", ArrayList.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("::::::::::::::: response.getBody() :::::::::::::::" + response.getBody());
            System.out.println("::::::::::::::: response.getBody() Class :::::::::::::::" + response.getBody().getClass());
            return GlobalResponse.responseBuilder4("::: successful :::", "01", response.getBody());
        } else {
            System.out.println("::::::::::::::: Request failed with status code ::::::::::: " + response.getStatusCodeValue());
            return GlobalResponse.responseBuilder4("::: unsuccessful :::", "100", null);
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


}

