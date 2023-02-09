/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.controller;

import com.iwomi.External_api_cccNewUp.model.MtnMomoConfig;
import com.iwomi.External_api_cccNewUp.model.Pwd;
import com.iwomi.External_api_cccNewUp.model.UserSession;
import com.iwomi.External_api_cccNewUp.model.Ussdfirstpage;
import com.iwomi.External_api_cccNewUp.model.labels;
import com.iwomi.External_api_cccNewUp.repository.LabelRepository;
import com.iwomi.External_api_cccNewUp.repository.MTNRepo;
import com.iwomi.External_api_cccNewUp.repository.PwdRepository;
import com.iwomi.External_api_cccNewUp.repository.UserSessionRepo;
import com.iwomi.External_api_cccNewUp.repository.UssdfirstpageRepository;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.apache.commons.lang3.StringUtils.trim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author user
 */
@RestController
@CrossOrigin()
public class ConfigController {

    @Autowired
    MTNRepo mtnRepo;
    
    @Autowired
    UserSessionRepo userSessionRepo;
    
    @Autowired
    PwdRepository pwdRepository;
    
    @Autowired
    LabelRepository labelRepository;
    
    @Autowired
    UssdfirstpageRepository ussdfirstpageRepository;

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public Map<String, String> config(@RequestBody Map<String, String> payload) {
        Map<String, String> response = new HashMap();
        /*
        cuser, muser
        type=momo_collect | momo_deposit | om |mtn_ussd | orange_ussd
        momo = {
        apikey,apiuser,subkey
        }
        om={
        apikey,apisecret,apipwd,apiuser,chmsd,pin
        }
        
        */
        if(payload.get("type").equalsIgnoreCase("momo_collect")){
            String apikey = payload.get("apikey");
            String apiuser = payload.get("apiuser");
            String subkey = payload.get("subkey");
            String code = "001";
            MtnMomoConfig mtnconfig = mtnRepo.findMTNByCode(code);
            mtnconfig.setApiKey(apikey);
            mtnconfig.setApiUser(apiuser);
            mtnconfig.setSrvid(subkey);
            mtnconfig.setCode(code);
            mtnconfig.setProd("COLLECT");
            
            mtnRepo.save(mtnconfig);
            response.put("status", "01");
            response.put("message", "Successfully saved");
            return response;
        }
        else if(payload.get("type").equalsIgnoreCase("momo_deposit")){
            String apikey = payload.get("apikey");
            String apiuser = payload.get("apiuser");
            String subkey = payload.get("subkey");
            String code = "002";
            MtnMomoConfig mtnconfig = mtnRepo.findMTNByCode(code);
            mtnconfig.setApiKey(apikey);
            mtnconfig.setApiUser(apiuser);
            mtnconfig.setSrvid(subkey);
            mtnconfig.setCode(code);
            mtnconfig.setProd("DEPOSIT");
            
            mtnRepo.save(mtnconfig);
            
            response.put("status", "01");
            response.put("message", "Successfully saved");
            return response;
            
        }
        else if(payload.get("type").equalsIgnoreCase("key")){
            String key = payload.get("key");
            if(key.length()!= 16){
                response.put("status", "100");
                response.put("message", "Key needs to be 16bits in size");
                return response;
                
            }
            
            Pwd pwd = pwdRepository.findByAcscd("0216", "0");
            byte[] encode = Base64.getEncoder().encode(trim(key).getBytes());
            String v = new String(encode);
            pwd.setPass(v);
            pwdRepository.save(pwd);
            
            response.put("status", "01");
            response.put("message", "Successfully saved");
            return response;
        }
        else if(payload.get("type").equalsIgnoreCase("om")){
            
        }
        else if(payload.get("type").equalsIgnoreCase("mtn_ussd")){
            
        }
        
        return response;
    }

    @RequestMapping(value = "/subscribeUserNew", method = RequestMethod.POST)
    public Map<String, String> subscribeUserNew(@RequestBody Map<String, String> payload) {
        Map<String, String> response = new HashMap();
        /*
        tel - without 237
        status  0 = validate Acccount attachment with OTP, 1 = level of change password, 2= account is blocked ,3 = account is normal, evreything ok
        subscribe_code
        
        */
        String tel = payload.get("tel");
        String status = payload.get("status");
        String subscribe_code = payload.get("subid");
        String lang = payload.get("lang");//default lang
        
        UserSession user = userSessionRepo.findClientByPhone(tel ).orElse(null);
        
        if(user!= null){
            //this user was desactivate, we simply reactivate and confirm the status,
            user.setDele("0");
            user.setPos(null);
            user.setPreval(null);
            user.setStatus(status);
            user.setMdfi(new Date());
            user.setNwpin(3);
            user.setNwotp(3);
            user.setInval(2);
            user.setLang(lang);
            userSessionRepo.save(user);

            response.put("status", "01");
            response.put("message", "Successfully Reactivated");
            return response;
        }
        else{
            UserSession user1 = new UserSession();
            
            user1.setPhone(tel);
            user1.setUuid(subscribe_code);
            user1.setDele("0");
            user1.setPos(null);
            user1.setPreval(null);
            user1.setStatus(status);
            user1.setCrtd(new Date());
            user1.setMdfi(new Date());
            user1.setNwpin(3);
            user1.setNwotp(3);
            user1.setInval(2);
            user1.setLang(lang);
            userSessionRepo.save(user1);

            response.put("status", "01");
            response.put("message", "Successfully Reactivated");
            return response;
            
        }
        

    }

    @RequestMapping(value = "/UpdateSubscribedUser", method = RequestMethod.POST)
    public Map<String, String> UpdateSubscribedUser(@RequestBody Map<String, String> payload) {
        Map<String, String> response = new HashMap();
        /*
        tel - without 237
        status  0 = validate Acccount attachment with OTP, 1 = level of change password, 2= account is blocked ,3 = account is normal, evreything ok
        subscribe_code
        
        */
        String tel = payload.get("tel");
        String status = payload.get("status");
        String dele = payload.get("dele");
        String subscribe_code = payload.get("subid");
        String lang = payload.get("lang");//default lang
        
        try{
             UserSession user = userSessionRepo.findClientByPhone(tel ).orElse(null);
        
            if(user!= null){
                user.setDele(dele);
                user.setPos(null);
                user.setPreval(null);
                user.setStatus(status);
                user.setMdfi(new Date());
                user.setNwpin(3);
                user.setNwotp(3);
                user.setInval(2);
                user.setLang(lang);
                userSessionRepo.save(user);

                response.put("status", "01");
                response.put("message", "Successfully Reactivated");
                return response;
            }
            else{
                response.put("status", "01");
                response.put("message", "User not found in the system, please check for correct phone number and subscription code");
                return response;
            }
            
        }
        catch(Exception e){
            response.put("status", "02");
            response.put("message", e.getMessage());
            return response;
        }
       

    }
    
    @RequestMapping(value = "/getConfig", method = RequestMethod.POST)
    public Map<String, Object> getConfig(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap();
        /*
        cuser, muser
        type=momo_collect | momo_deposit | om |mtn_ussd | orange_ussd
        momo = {
        apikey,apiuser,subkey
        }
        om={
        apikey,apisecret,apipwd,apiuser,chmsd,pin
        }
        
        */
        if(payload.get("type").equalsIgnoreCase("momo")){
            List<Map<String, String>> dat= new ArrayList();
            
            MtnMomoConfig mtnconfig = mtnRepo.findMTNByCode("001");
            
            Map<String, String> data = new HashMap();
            data.put("apikey", mtnconfig.getApiKey());
            data.put("apiuser", mtnconfig.getApiUser());
            data.put("subkey", mtnconfig.getSrvid());
            data.put("type", "momo_collect");
            dat.add(data);
            
            MtnMomoConfig mtnconfig1 = mtnRepo.findMTNByCode("002");
            
            Map<String, String> data2 = new HashMap();
            data2.put("apikey", mtnconfig1.getApiKey());
            data2.put("apiuser", mtnconfig1.getApiUser());
            data2.put("subkey", mtnconfig1.getSrvid());
            data2.put("type", "momo_deposit");
            dat.add(data2);
            
            response.put("status", "01");
            response.put("data", dat);
            response.put("message", "Successfully saved");
            return response;
        }
        else if(payload.get("type").equalsIgnoreCase("key")){
            Pwd pwd = pwdRepository.findByAcscd("0216", "0");
            byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass().toString()));
            String v = new String(decoder);
            final String key = trim(v);
            
            response.put("status", "01");
            response.put("data", key);
            response.put("message", "Successfully saved");
            return response;
            
        }
        else if(payload.get("type").equalsIgnoreCase("om")){
            
        }
        else if(payload.get("type").equalsIgnoreCase("mtn_ussd")){
            
        }
        
        return response;
    }

    @RequestMapping(value = "/configLabels", method = RequestMethod.POST)
    public Map<String, String> configLabels(@RequestBody Map<String, String> payload){
        Map<String, String> response = new HashMap();
        System.out.println("Yann see this payload"+payload);
        String key = payload.get("key").trim();
        String valen = payload.get("valen");
        String valfr = payload.get("valfr");
        String muser = payload.get("muser");
        String dele = "0";
        
        
        
        labels label = labelRepository.findByKey(key,dele);
        if(label != null ){
            label.setValen(valen);
            label.setValfr(valfr);
            label.setMdfi(muser);
            label.setCrtd(new Date().toString());
            labelRepository.save(label);
            
            response.put("status","01" );
            response.put("message", "Successfully updated");
            return response;
            
        }
        
        response.put("status","02" );
        response.put("message", "Label Key not found in our system");
        
        return response;
    }

    
    @RequestMapping(value = "/getAllLabels", method = RequestMethod.GET)
    public Map<String, Object> getAllLabels(){
        Map<String, Object> response = new HashMap();
        
        String dele = "0";
        List<labels> label = labelRepository.findALL(dele);
        
        
        response.put("status","01" );
        response.put("data",label);
        response.put("message", "Successfully updated");
        return response;
        
    }
    
    
    @RequestMapping(value = "/getLabelByKey", method = RequestMethod.POST)
    public Map<String, Object> getLabelByKey(@RequestBody Map<String, String> payload){
        Map<String, Object> response = new HashMap();
        String key = payload.get("key");
        String dele = "0";
        labels label = labelRepository.findByKey(key,dele);
        if(label != null ){
            
            response.put("status","01" );
            response.put("data", label);
            response.put("message", "Successfully updated");
            return response;
            
        }
        
        response.put("status","02" );
        response.put("message", "Label Key not found in our system");
        
        return response;
    }
    
    
    @RequestMapping(value = "/getMenuPage", method = RequestMethod.GET)
    public Map<String, Object> getMenuPage(){
        Map<String, Object> response = new HashMap();
        
        String dele = "0";
        List <Ussdfirstpage> menu = ussdfirstpageRepository.findAll();
        
        
        response.put("status","01" );
        response.put("data",menu);
        response.put("message", "Successfully updated");
        return response;
        
    }
    
    
    @RequestMapping(value = "/configMenuItem", method = RequestMethod.POST)
    public Map<String, Object> configMenuItem(@RequestBody Map<String, String> payload){
        Map<String, Object> response = new HashMap();
        String id = payload.get("id");
        String active = payload.get("active"); //1 = active 0 = desactivated
        String valen = payload.get("valen");
        String valfr = payload.get("valfr");
        
        Ussdfirstpage menu = ussdfirstpageRepository.findById(Integer.parseInt(id)).orElse(null);
        if(menu != null ){
            menu.setActive(active);
            menu.setValen(valen);
            menu.setValfr(valfr);
            ussdfirstpageRepository.save(menu);
            
            response.put("status","01" );
            response.put("message", "Successfully configured");
            return response;
            
        }
        
        response.put("status","02" );
        response.put("message", "Menu Item not found in our system");
        
        return response;
    }
    
    

}

