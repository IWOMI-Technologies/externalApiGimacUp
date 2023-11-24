/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author TAGNE
 */
@Service
public class ApiGimacFirstTrustService {
    
     @Value("${db.urlGimac}")
    private String urif;
    
    public JSONObject post_method(String path ,Map<String, String> request, String token) {
        
        //String uri = this.urif +path;
         String uri =path;
        
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        JSONObject result = new JSONObject();
        System.out.println("yann see this "+ response.toString());
        
        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            return res;
        }
        result.put("status", "503");
        result.put("message","Service temporary unavailable");
        return result;

    }
    
     public JSONObject post_methodOK(String path ,Map<String, String> request, String token) {
        
       // String uri = this.urif +path;
        String uri =path;
        
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        JSONObject result = new JSONObject();
        System.out.println("yann see this "+ response.toString());
        
        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            return res;
        }
        result.put("status", "503");
        result.put("message","Service temporary unavailable");
        return result;

    }
    
     public JSONObject post_methodNt(String path ,Map<String, String> request) {
        
       // String uri = this.urif +path;
        String uri =path;
        
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        JSONObject result = new JSONObject();
        System.out.println("yann see this "+ response.toString());
        
        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            return res;
        }
        result.put("status", "503");
        result.put("message","Service temporary unavailable");
        return result;

    }
     public JSONObject post_methodNtF(String path ,Map<String, String> request) {
        
       // String uri = this.urif +path;
        String uri =path;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //header
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        JSONObject result = new JSONObject();
        System.out.println("yann see this "+ response.toString());
        
        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            return res;
        }
        result.put("status", "503");
        result.put("message","Service temporary unavailable");
        return result;

    }
    
     public JSONObject post_methodOb(String path ,Map<String, Object> request, String token) {
        
       // String uri = this.urif +path;
        String uri =path;
        System.out.println("yvo see this url"+ uri);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
       
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
          Map< String, Object> response2 = new HashMap();
       // response2 = jsonToMap(request);
        System.out.println("yvo le map1:" + request.toString());
       // HttpEntity< Map< String, Object>> entity = new HttpEntity<>(response2, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        JSONObject result = new JSONObject();
        System.out.println("yann see this "+ response.toString());
        
        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            return res;
        }
        result.put("status", "503");
        result.put("message","Service temporary unavailable");
        return result;

    }
   
     public JSONObject get_method(String path , String token) {
        
       // String uri = this.urif +path;
        String uri =path;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        }
            catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        } 
                catch (KeyManagementException ex) {
            Logger.getLogger(ApiGimacFirstTrustService.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        JSONObject result = new JSONObject();
        System.out.println("yann see this "+ response.toString());
        
        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            return res;
        }
        result.put("status", "503");
        result.put("message","Service temporary unavailable");
        return result;

    }
     public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
}
        };
       // SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        
      //  SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
       // CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        
        
        //HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
       // requestFactory.setHttpClient(httpClient);
        //RestTemplate restTemplate = new RestTemplate(requestFactory);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    
}
