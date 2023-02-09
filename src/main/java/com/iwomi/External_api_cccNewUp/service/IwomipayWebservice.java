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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.json.JSONObject;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author user
 */
@Service
public class IwomipayWebservice{

    
    final String uri = "https://www.pay.iwomitechnologies.com:8443/iwomi_pay_prodv2";
   

    public JSONObject post_method(String path ,Map<String, String> request, String token, String account_key) {
        
        String uri = this.uri +path;
        
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+token);
        headers.add("AccountKey", account_key);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(IwomipayWebservice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(IwomipayWebservice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(IwomipayWebservice.class.getName()).log(Level.SEVERE, null, ex);
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
    

    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] x509Certificates, String s) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        
        
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        //requestFactory.setHttpClient(httpClient);
       RestTemplate restTemplate = new RestTemplate(requestFactory);
       // RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    

}
