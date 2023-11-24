/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.controller;

import com.google.common.net.MediaType;
import com.iwomi.External_api_cccNewUp.service.ApiGimacFirstTrustService;
import com.iwomi.External_api_cccNewUp.ussd.service.SendEmailTLS;
import io.micrometer.core.ipc.http.HttpSender.Request;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TAGNE
 */
@RestController
//@CrossOrigin()
@RequestMapping("${apiPrefix}")
public class ApiGimacController {
    
    @Autowired
    ApiGimacFirstTrustService apiGimacFirstTrustService;
     @Autowired
    SendEmailTLS sendEmailTLS;
    
   @RequestMapping(value = "/apiTokenGimac", method = RequestMethod.POST)
    public Map<String, Object> apiTokenGimac( @RequestHeader(value = "path") String path, @RequestBody Map<String, String> payload) throws NoSuchAlgorithmException {
        return apiGimacFirstTrustService.post_methodNt(path, payload).toMap();

    }
    @RequestMapping(value = "/apiTokenGimacForm", method = RequestMethod.POST)
    public Map<String, Object> apiTokenGimacForm( @RequestHeader(value = "path") String path, @RequestBody Map<String, String> payload) throws NoSuchAlgorithmException {
        return apiGimacFirstTrustService.post_methodNtF(path, payload).toMap();
   }
    
     @RequestMapping(value = "/apiServicePostGimac", method = RequestMethod.POST)
    public Map<String, Object> apiServiceGimac( @RequestHeader(value = "access_token") String token, @RequestHeader(value = "path") String path, @RequestBody Map<String, String> payload) throws NoSuchAlgorithmException {
        return apiGimacFirstTrustService.post_method(path, payload, token).toMap();

    }
     @RequestMapping(value = "/apiServiceGetGimac", method = RequestMethod.POST)
    public Map<String, Object> apiServiceGetGimac( @RequestHeader(value = "access_token") String token, @RequestHeader(value = "path") String path) throws NoSuchAlgorithmException {
        return apiGimacFirstTrustService.get_method(path,  token).toMap();

    }
     @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public Map<String, Object> sendEmail(@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException {
         String sms = payload.get("msg");
       String obj = payload.get("obj");
        String email = payload.get("ema");
        return sendEmailTLS.sendEmailApi(email, sms, obj).toMap();

    }
    
   
    
}
