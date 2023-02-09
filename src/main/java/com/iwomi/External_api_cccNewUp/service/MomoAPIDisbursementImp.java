/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwomi.External_api_cccNewUp.model.DepositObject;
import com.iwomi.External_api_cccNewUp.model.Payee;
import com.iwomi.External_api_cccNewUp.serviceInterface.MomoDisbursementAPI;
import io.jsonwebtoken.impl.TextCodec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author user
 */
@Service
public class MomoAPIDisbursementImp implements MomoDisbursementAPI {

    //final String uri = "https://sandbox.momodeveloper.mtn.com/disbursement/";
    //String uri = "https://ericssonbasicapi1.azure-api.net/disbursement/";
    
    String uri = "https://proxy.momoapi.mtn.com/disbursement/";
    
    @Autowired
    GeneralUtilsServices generalUtilsServices;

    @Override
    public String getToken(String apiUser, String apiKey, String subKey) {
        
        try {
            apiUser = generalUtilsServices.decrypt_data(apiUser);
            apiKey = generalUtilsServices.decrypt_data(apiKey);
            subKey = generalUtilsServices.decrypt_data(subKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(MomoAPIDisbursementImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        String uri = this.uri + "token/";
        String token = "";
        String request = "";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // add basic authentication
            headers.add("Ocp-Apim-Subscription-Key", subKey);
            headers.add("Authorization", "Basic " + TextCodec.BASE64.encode(apiUser + ":" + apiKey));

            HttpEntity<String> entity = new HttpEntity<String>(request, headers);

            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = createRestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

            System.out.println("This is the status " + response.getStatusCodeValue());

            if (response.getStatusCodeValue() == 200) {
                return response.getBody();
            } else if (response.getStatusCodeValue() == 401) {
                System.out.println("unauthorized");
                return "FAILED";
            } else if (response.getStatusCodeValue() == 500) {

                System.out.println("internal server error");
                return "FAILED";
            } else {
                String result = response.getBody();
                System.out.println(result);
                return "FAILED";
            }
        } catch (HttpClientErrorException ex) {
            Logger.getLogger(MomoAPICollectionImp.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error from server during checkstatus request ");
        } catch (Exception ex) {
            Logger.getLogger(MomoAPIDisbursementImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "FAILED";
    }

    @Override
    public Map<String, String> performPayment(String amount, String number, String payerMsg, String payeeMsg, String currency, String token, String subKey,String externalId) {
        try {
            
            subKey = generalUtilsServices.decrypt_data(subKey);
            
            System.out.println(token+" , ");
            DepositObject depositObject;
            Payee payee;
            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = createRestTemplate();
            payee = new Payee("MSISDN", number);
            depositObject = new DepositObject(amount, currency, externalId, payee, payerMsg, payeeMsg);
            
            Map<String, String> response = new HashMap<>();
            
            ObjectMapper mapper = new ObjectMapper();
            String json = "";
            try {
                json = mapper.writeValueAsString(depositObject);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(MomoAPICollectionImp.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(json);
            
            UUID uuid = UUID.randomUUID();
            System.out.println(" information to do request payment test:"+token+" , "+uuid);
            String uri = this.uri + "v1_0/transfer";
//        String uri = requestUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", "Bearer " + token);
        //headers.add("X-Callback-Url", "localhost:8080/checkstatustreate");
        headers.add("X-Reference-Id", uuid.toString());
        //headers.add("X-Target-Environment", "sandbox");
        headers.add("X-Target-Environment", "mtncameroon");
        headers.add("Ocp-Apim-Subscription-Key", subKey);

        HttpEntity<DepositObject> entity = new HttpEntity<DepositObject>(depositObject, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);

        System.out.println("Yanick see the status code " + result.getStatusCodeValue());

        // Code = 200.
        String internal_id = uuid.toString();
        if (result.getStatusCodeValue() == 202) {
            response.put("referenceId", internal_id);
            response.put("success", "01");
            response.put("status", "01");
            response.put("internal_id", internal_id);
            response.put("data", result.getBody());

        } else {
            response.put("success", "100");
        }

        System.out.println("Yanick please see this " + response);
        return response;
        } catch (Exception ex) {
            Logger.getLogger(MomoAPIDisbursementImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    @Override
    public String OperationStatus(String referenceId, String subKey, String token) {
        try {
            
            subKey = generalUtilsServices.decrypt_data(subKey);
            
            String uri = this.uri + "v1_0/transfer/" + referenceId;
            
            String request = "";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // add basic authentication
            headers.add("Ocp-Apim-Subscription-Key", subKey);
            //headers.add("X-Target-Environment", "sandbox");
            headers.add("Authorization", "Bearer " + token);
            headers.add("X-Target-Environment", "mtncameroon");
            
            HttpEntity<String> entity = new HttpEntity<String>(request, headers);
            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = createRestTemplate();
            
            ResponseEntity<String> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity,
                    String.class);
            //ResponseEntity<String> response = restTemplate.getForEntity(uri, entity, String.class);
            
            System.out.println("This is the status " + response.getStatusCodeValue());
            
            if (response.getStatusCodeValue() == 200) {
                return response.getBody();
            } else if (response.getStatusCodeValue() == 401) {
                return "unauthorized";
            } else if (response.getStatusCodeValue() == 500) {
                return "internal server error";
            } else {
                String result = response.getBody();
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(MomoAPIDisbursementImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Override
    public String getAccountBalance(String subKey,String token) {
        try {
            
            subKey = generalUtilsServices.decrypt_data(subKey);
            
            String uri = this.uri + "v1_0/account/balance";
            
            String request = "";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // add basic authentication
            headers.add("Ocp-Apim-Subscription-Key", subKey);
            //headers.add("X-Target-Environment", "sandbox");
            headers.add("Authorization", "Bearer " + token);
            headers.add("X-Target-Environment", "mtncameroon");
            
            HttpEntity<String> entity = new HttpEntity<String>(request, headers);
            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = createRestTemplate();
            
            ResponseEntity<String> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity,
                    String.class);
            //ResponseEntity<String> response = restTemplate.getForEntity(uri, entity, String.class);
            
            System.out.println("This is the status " + response.getStatusCodeValue());
            
            if (response.getStatusCodeValue() == 200) {
                return response.getBody();
            } else if (response.getStatusCodeValue() == 400) {
                return "unauthorized";
            } else if (response.getStatusCodeValue() == 500) {
                return "internal server error";
            } else {
                String result = response.getBody();
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(MomoAPIDisbursementImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String MomoAccountExist(String accountHolderId, String subKey, String token) {
        try {
            
            subKey = generalUtilsServices.decrypt_data(subKey);
            
            String uri = this.uri + "v1_0/accountholder/" + "msisdn" + "/" + accountHolderId + "/active";
            
            String request = "";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // add basic authentication
            headers.add("Ocp-Apim-Subscription-Key", subKey);
            //headers.add("X-Target-Environment", "sandbox");
            headers.add("Authorization", "Bearer " + token);
            headers.add("X-Target-Environment", "mtncameroon");
            
            HttpEntity<String> entity = new HttpEntity<String>(request, headers);
            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = createRestTemplate();
            
            ResponseEntity<String> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity,
                    String.class);
            //ResponseEntity<String> response = restTemplate.getForEntity(uri, entity, String.class);
            
            System.out.println("This is the status " + response.getStatusCodeValue());
            
            if (response.getStatusCodeValue() == 200) {
                return response.getBody();
            } else if (response.getStatusCodeValue() == 400) {
                return "unauthorized";
            } else if (response.getStatusCodeValue() == 500) {
                return "internal server error";
            } else {
                String result = response.getBody();
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(MomoAPIDisbursementImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    private RestTemplate createRestTemplate() throws Exception {
        final String username = "iwomi";
        final String password = "";
        final String proxyUrl = "10.100.18.1";
        final int port = 3128;

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials( 
            new AuthScope(proxyUrl, port), 
            new UsernamePasswordCredentials(username, password)
        );

        HttpHost myProxy = new HttpHost(proxyUrl, port);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider).disableCookieManagement();

        HttpClient httpClient = clientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        //factory.setHttpClient(httpClient);

        //return new RestTemplate(factory);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

}

