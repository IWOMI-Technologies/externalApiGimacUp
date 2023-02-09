/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.controller;

import com.iwomi.External_api_cccNewUp.cofig.TraceService;
import com.iwomi.External_api_cccNewUp.model.TransactionHistory;
import com.iwomi.External_api_cccNewUp.repository.TransHisRepository;
import com.iwomi.External_api_cccNewUp.service.IwomipayWebservice;
import com.iwomi.External_api_cccNewUp.service.MomoAPICollectionImp;
import com.iwomi.External_api_cccNewUp.service.MomoAPIDisbursementImp;
import com.iwomi.External_api_cccNewUp.serviceInterface.OperationApi;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author user
 */
@RestController
@CrossOrigin("*")
public class IWOMIPAYController {
    
    @Autowired
    IwomipayWebservice iwomipayWebservice;
    
    @Autowired
    MomoAPICollectionImp momoAPICollectionImp;
    
    @Autowired
    TransHisRepository transactionHistroyRep,transactionHistroy;
    
    @Autowired
    OperationApi operationApi;
    
    @Autowired
    MomoAPIDisbursementImp momoAPIDisbursementImp;
    
    /*
    final String uri = "https://www.pay.iwomitechnologies.com:8443/iwomi_pay_prodv2";
    String username = "20CCC411032";
    String password = "20120CCC41103212";
    
    
    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public Map<String, Object> request_payment( @RequestBody Map<String, String> payload) throws NoSuchAlgorithmException{
        
        //type, amount , tel, external_id
        
        payload.put("op_type", "credit");
        payload.put("motif", "Collect Operation CCC");
        String account_key = "";
        
        Map<String, Object> result = new HashMap();
        String token = getToken();
        System.out.println(token);
        if(token.equalsIgnoreCase("failed")){
            result.put("status","503");
            result.put("message","token invalid, authentication failed");
            return result;
        }
        String op_type = payload.get("type");
        if(op_type.equalsIgnoreCase("momo")){
            account_key =TextCodec.BASE64.encode( "8d50bf4f-23c3-437a-ad16-055bf1d6f9d5:bec58abb-44c2-4cde-b6d8-8c4560e38dbd");
            
        }
        else if(op_type.equalsIgnoreCase("om")){
            account_key = TextCodec.BASE64.encode("2daa1043-9f32-4ae7-96d0-5b096bc9a7e2:772b39d2-c513-4535-9cfb-5ecd2a9e04c0");
        }
        else{
            result.put("status","404");
            result.put("message","Payment type not found");
            return result;
        }
        String path = "/iwomipay";
        return iwomipayWebservice.post_method(path, payload, token, account_key).toMap();

    }
    
    
    @RequestMapping(value = "/payout", method = RequestMethod.POST)
    public Map<String, Object> payout( @RequestBody Map<String, String> payload) throws NoSuchAlgorithmException{
        
        //type, amount , tel, external_id
        
        payload.put("op_type", "debit");
        
        payload.put("motif", "Payout Operation CCC");
        String account_key = "";
        
        Map<String, Object> result = new HashMap();
        String token = getToken();
        if(token.equalsIgnoreCase("failed")){
            result.put("status","503");
            result.put("message","token invalid, authentication failed");
            return result;
        }
        String op_type = payload.get("type");
        if(op_type.equalsIgnoreCase("momo")){
            account_key = TextCodec.BASE64.encode( "57d6e877-9d91-4fc5-80aa-0262773be579:c4fd360c-20ff-4ac6-8a6e-25037c73dfd2");
            
        }
        else if(op_type.equalsIgnoreCase("om")){
            account_key = TextCodec.BASE64.encode("6db46c9c-6396-4f29-a6d0-91096f1e3e04:19178e11-abde-4bae-991d-ba4481393724");
        }
        else{
            result.put("status","404");
            result.put("message","Payment type not found");
            return result;
        }
        String path = "/iwomipay";
        return iwomipayWebservice.post_method(path, payload, token, account_key).toMap();

    }
    
    
    
    
    
    @RequestMapping(value = "/iwomipayStatus", method = RequestMethod.POST)
    public Map<String, Object> iwomipayStatus(@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException {
        
        String internal_id = payload.get("internal_id");
        String uri = this.uri + "/iwomipayStatus/" + internal_id;
        String token = "";
        
        Map<String, Object> result = new HashMap();
        token = getToken();
        if(token.equalsIgnoreCase("failed")){
            result.put("status","503");
            result.put("message","token invalid, authentication failed");
            return result;
        }
        
        String request = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // add basic authentication
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<String>(request, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);
        
        if (response.getStatusCodeValue() == 200) {
             JSONObject res = new JSONObject(response.getBody());
             return res.toMap();
         }
        result.put("status", "503");
        result.put("message","Service temporary unavailable");
        return result;
       
    }
    
    
    
    
    
    public String getToken() throws NoSuchAlgorithmException{
       
        JSONObject result = new JSONObject();
        try {
            
            String path = "/authenticate";
            
            Map<String, String> request = new HashMap();
            
            request.put("username", username);
            request.put("password", password);
            
            
            String url = this.uri +path;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            
            System.out.println("yann see this "+response.getStatusCodeValue());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                
                if(res.get("status").toString().equalsIgnoreCase("01")){
                    return res.get("token").toString();
                }
                else{
                    return "failed";
                }
            }
            return "failed";
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(IWOMIPAYController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(IWOMIPAYController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "failed";


    }
    
    */
    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] x509Certificates, String s) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        
        
        
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
       // requestFactory.setHttpClient(httpClient);
        //RestTemplate restTemplate = new RestTemplate(requestFactory);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
   
    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public Map<String, String> performMtnMOMO(@RequestBody Map<String, String> payload1) throws Exception {
        
        payload1.put("motive", "Mobile Banking CCC PLC");
        
        /*
        
        {
            "tel":"237675669236",
            "amount":"1",
            "type":"momo",
            "external_id":"test_ccc"
        }
        */
        
        /*
        
        {
            "amount": "2000",
            "external_id": "yan0021",
            "tel":"46733123450",
            "motive": "Hello this is yanick"
        }
        */
        
        
        
        String name = new TraceService().getInstance();
        System.out.println("Yanick this is the name of the user extracted from the token consuming this **"+name);
        
        int o = 4;
        Map<String, String> payload = convert_SoftellerRe(payload1);
        System.out.println("pls 1");

        Map<String, String> pps = operationApi.getAccessToApI2("001");
        System.out.println("pls 3");

        if (!pps.isEmpty()) {
            String data = momoAPICollectionImp.getToken(pps.get("apiUser"), pps.get("apiKey"), pps.get("subKey"));
            System.out.println("pls 2");
            if (!data.equalsIgnoreCase("FAILED")) {
                JSONObject ss = new JSONObject(data);
                Map<String, String> requestpay = momoAPICollectionImp.performRedrawal(payload.get("amount"), payload.get("number"),
                        payload.get("payerMsg"), payload.get("payeeMsg"), payload.get("currency"),
                        ss.getString("access_token"), pps.get("subKey"), payload1.get("externalId"));
                payload.put("token", ss.getString("access_token"));
                payload.put("subKey", pps.get("subKey"));
                System.out.println("it is the payload content");
                System.out.println(payload);
                if (requestpay.get("success").equals("01")) {
                    Date date = new Date();
                    TransactionHistory s = new TransactionHistory(payload.get("amount"),name, payload.get("currency"), payload.get("externalId"), payload.get("number"), payload.get("payerMsg"), payload.get("payeeMsg"),
                            "1000", requestpay.get("referenceId"), "TR", "", "", date.toString());
                    s.setCmptbkst(pps.get("cmptbkst"));
                    s.setCodser(pps.get("codser"));
                    transactionHistroy.save(s);
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            // continuesly do a check status till it get a favorable response
                            int i = 0;
                            boolean found = false;
                            do {
                                try {
                                    Thread.sleep(30000L);
                                } catch (InterruptedException e1) {
                                }
                                found = checkstatusOperationCollV1(payload, requestpay, "001");
                                i++;
                                System.out.println("count " + i);
                            } while (i < o && !found);
                            if (found || i == o) {
                                // function to call send to him message
                                
                                System.out.println("operation successful");

                            }
                        }
                    ;

                    });
			t.start();
                        
                    
                    requestpay.put("status", "1000");
                    return requestpay;
                } else {
                    //return fail and stop
                    return null;
                }

            } else {
                //no token. stop
                return null;
            }

        } else {
            //notp no found on database
            return null;
        }

    }
    @RequestMapping(value = "/payout", method = RequestMethod.POST)
    public Map<String, String> makeDeposit_v1(@RequestBody Map<String, String> payload1) throws Exception {
        
        String name = new TraceService().getInstance();
        int o = 4;
        Map<String, String> payload = convert_SoftellerRe(payload1);
        System.out.println("pls 1");

        Map<String, String> pps = operationApi.getAccessToApI3("002");
        System.out.println("pls 3");
        System.out.println(pps.get("apiUser"));
        System.out.println(pps.get("apiKey"));
        System.out.println(pps.get("subKey"));

        if (!pps.isEmpty()) {
            String data = momoAPIDisbursementImp.getToken(pps.get("apiUser"), pps.get("apiKey"), pps.get("subKey"));
            System.out.println("pls 2");
            if (!data.equalsIgnoreCase("FAILED")) {

                JSONObject ss = new JSONObject(data);
                /***we check the balance of our account before proceeding****/
                /*
                String balanceMap = momoAPIDisbursementImp.getAccountBalance(pps.get("subKey"),ss.getString("access_token"));

                JSONObject balance = new JSONObject(balanceMap);
                if (balance.has("availableBalance")) {
                    
                    if( Double.parseDouble(payload.get("amount"))  >Double.parseDouble(balance.getString("availableBalance"))){
                        Map <String, String> response = new HashMap();
                        //iwomi has small balance to do the payment
                        response.put("success", "100");
                        response.put("message", "IWOMI has run out of funds");
                        return response;
                    }
                }
                */
                Map<String, String> requestpay
                        = momoAPIDisbursementImp.performPayment(payload.get("amount"), payload.get("number"),
                                payload.get("payerMsg"), payload.get("payeeMsg"), payload.get("currency"),
                                ss.getString("access_token"), pps.get("subKey"), payload1.get("externalId"));
                payload.put("token", ss.getString("access_token"));
                payload.put("subKey", pps.get("subKey"));
                System.out.println("it is the payload content");
                System.out.println(payload);
                if (requestpay.get("success").equals("01")) {
                    Date date = new Date();
                    TransactionHistory s = new TransactionHistory(payload.get("amount"), name, payload.get("currency"),
                            payload.get("externalId"), payload.get("number"), payload.get("payerMsg"), payload.get("payeeMsg"),
                            "1000", requestpay.get("referenceId"), "DE", "", "", date.toString());
                    s.setCmptbkst(pps.get("cmptbkst"));
                    s.setCodser(pps.get("codser"));
                    transactionHistroy.save(s);
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            // continuesly do a check status till it get a favorable response
                            int i = 0;
                            boolean found = false;
                            do {
                                try {
                                    Thread.sleep(500L);
                                } catch (InterruptedException e1) {
                                }
                                found = checkstatusOperationDepV1(payload, requestpay, "002");
                                i++;
                                System.out.println("count " + i);
                            } while (i < o && !found);

                            if (found) {
                                // function to call send to him message
                                System.out.println("operation successful");
                            }
                        }
                    ;

                    });
			t.start();
                    
                    requestpay.put("status", "1000");
                    return requestpay;
                } else {
                    //return fail and stop
                    return null;
                }

            } else {
                //no token. stop
                return null;
            }

        } else {
            //notp no found on database
            return null;
        }

    }
    
    @RequestMapping(value = "/iwomipayStatus", method = RequestMethod.POST)
    public Map<String, String> iwomipayStatus(@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException {
        
        String internal_id = payload.get("internal_id");
        
        Map<String, String> result = new HashMap();
        
        TransactionHistory r = transactionHistroy.findBy_Reference_id(internal_id);
        if(r != null){
            
            result.put("status", r.getStatus());
            result.put("message", r.getReason());
            result.put("internal_id", internal_id);
            result.put("external_id", r.getExternalId());
            return result;
        }
        
        
        result.put("status", "404");
        result.put("message", "Transaction not found in our system");
        
        return result;
        

    }

    private Map<String, String> convert_SoftellerRe(Map<String, String> p) {
        p.put("number", p.get("tel"));
        p.put("externalId", p.get("external_id"));
        p.put("payerMsg", p.get("motif"));
        p.put("payeeMsg", "softeller");
        p.put("currency", "XAF");
        p.remove("tel");
        p.remove("motif");
        p.remove("external_id");
        return p;
    }
    
    
    private boolean checkstatusOperationCollV1(Map<String, String> payload, Map<String, String> requestPay, String oo) {
        Boolean found = false;
        Map<String, String> status;
        String status1 = momoAPICollectionImp.OperationStatus(requestPay.get("referenceId"), payload.get("subKey"),
                payload.get("token"));
        if (!status1.equalsIgnoreCase("FAILED")) {
            JSONObject data = new JSONObject(status1);
            System.out.println(data);
            System.out.println("status of the transaction : " + data.getString("status"));
            if (data.getString("status").equals("SUCCESSFUL")) {// successfull transaction
                found = true;
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "01", "SUCCESSFUL");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (data.getString("status").equals("FAILED") || data.getString("status").equals("REJECTED")
                    || data.getString("status").equals("TIMEOUT")) {
                try {
                    System.out.println("status of the transaction : " + data.getString("status"));
                    operationApi.updateStatus2(requestPay.get("referenceId"), "100", data.getString("reason"));
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else if (data.getString("status").equals("PENDING")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "1000", "PENDING");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else if (data.getString("status").equals("ONGOING")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "1000", "ONGOING");
                    try {
                        Thread.sleep(30000L);
                        found = checkstatusOperation(payload, requestPay, oo);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else {
                found = false;
            }
        } else {
            found = false;
        }

        return found;
    }
    
    private boolean checkstatusOperation(Map<String, String> payload, Map<String, String> requestPay, String oo) {
        Boolean found = false;
        Map<String, String> status;
        status = operationApi.OperationStatus1(requestPay.get("referenceId"), payload.get("subKey"),
                payload.get("token"), oo);
        System.out.println("it is the steps of entry");
        if (status.get("statuscode").equals("200")) {
            JSONObject data = new JSONObject(status.get("data"));
            System.out.println(data);
            System.out.println("status recieved :" + data.getString("status"));
            if (data.getString("status").equals("SUCCESSFUL")) {// successfull transaction
                found = true;
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "01", "");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (data.getString("status").equals("FAILED") || data.getString("status").equals("REJECTED")
                    || data.getString("status").equals("TIMEOUT")) {
                try {
                    System.out.println("status in system");
                    operationApi.updateStatus2(requestPay.get("referenceId"), "100", data.getString("reason"));
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else if (data.getString("status").equals("PENDING")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "1000", "PENDING");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else if (data.getString("status").equals("ONGOING")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "1000", "ONGOING");
                    try {
                        Thread.sleep(30000L);
                        found = checkstatusOperation(payload, requestPay, oo);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else {
                found = false;
            }
        } else {
            found = false;
        }

        return found;
    }
    
    private boolean checkstatusOperationDepV1(Map<String, String> payload, Map<String, String> requestPay, String oo) {
        Boolean found = false;
        Map<String, String> status;
        String status1 = momoAPIDisbursementImp.OperationStatus(requestPay.get("referenceId"), payload.get("subKey"),
                payload.get("token"));
        if (!status1.equalsIgnoreCase("FAILED")) {
            JSONObject data = new JSONObject(status1);
            System.out.println(data);
            if (data.getString("status").equals("SUCCESSFUL")) {// successfull transaction
                found = true;
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "01", "SUCCESSFUL");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (data.getString("status").equals("FAILED") || data.getString("status").equals("REJECTED")
                    || data.getString("status").equals("TIMEOUT")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "100", data.getString("reason"));
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else if (data.getString("status").equals("PENDING")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "1000", "PENDING");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else if (data.getString("status").equals("ONGOING")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "1000", "ONGOING");
                    try {
                        Thread.sleep(30000L);
                        found = checkstatusOperation(payload, requestPay, oo);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else {
                found = false;
            }
        } else {
            found = false;
        }

        return found;
    }

}
