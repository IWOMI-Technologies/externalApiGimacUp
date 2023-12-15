/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.controller;

import com.iwomi.External_api_cccNewUp.model.UserSession;
import com.iwomi.External_api_cccNewUp.model.Ussdfirstpage;
import com.iwomi.External_api_cccNewUp.model.labels;
import com.iwomi.External_api_cccNewUp.model.responses;
import com.iwomi.External_api_cccNewUp.repository.LabelRepository;
import com.iwomi.External_api_cccNewUp.repository.UserSessionRepo;
import com.iwomi.External_api_cccNewUp.repository.UssdfirstpageRepository;
import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import com.iwomi.External_api_cccNewUp.ussd.config.UssdConfigFile;
import com.iwomi.External_api_cccNewUp.ussd.service.SendUssdUtils;
import com.iwomi.External_api_cccNewUp.ussd.service.UssdFirstTrustService;
import com.iwomi.External_api_cccNewUp.ussd.service.UssdService;
import com.iwomi.External_api_cccNewUp.ussd.utils.ManageUssdPosition;
import com.iwomi.External_api_cccNewUp.ussd.utils.MessageUssdUtils;
import com.iwomi.External_api_cccNewUp.ussd.utils.UtilsUssd;
import com.iwomi.External_api_cccNewUp.ussd.utils.UtilsUssdscpg;
import io.micrometer.core.annotation.Timed;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author TAGNE
 */
@RestController
@RequestMapping("${apiPrefix}")
@Component
@RequiredArgsConstructor
public class USSDFirstTrustController {
    @Value("${digitalbackoffice.url}")
    private String digitalUrl;
    
//    @Autowired
//    ManageUssdPosition manageuserposition;
//    
    @Autowired
    LabelRepository labelRepository;
//    
    @Autowired
    UssdfirstpageRepository ussdfirstpageRepository;
//    
//    @Autowired
//    UssdService ussd;
    @Autowired
   UserSessionRepo usersRepo;
//    @Autowired
//    MessageSource messageSource;
    @Autowired
    UssdFirstTrustService ussdFirstTrustService;
    

      @RequestMapping(value = "/endpoint" , method =  RequestMethod.POST)
   // @Timed
   // @Transactional(timeout = 1200)
   ResponseEntity<String> enpoint(@RequestBody Map<String, Object> payload) {
        System.out.println("yvo login Test3 de USSD:  "+payload.toString());
        String msisdn=checkPayload(payload, "msisdn").toString();
        String sessionid= checkPayload(payload, "sessionid").toString();
        String message=checkPayload(payload, "message").toString();
        String provider=checkPayload(payload, "provider").toString();
         JSONObject map = new JSONObject();
         String newLine = System.getProperty("line.separator");
         //String send="Welcome to First Trust" + newLine +" 2: Balance "+ newLine +" 3: Purchase "+ newLine +" 4: Statement "+ newLine +" 5: Enquiry "+ newLine +" 0: Exit";

       final UserSession user = usersRepo.findClientByPhone(msisdn).orElse(null);
       // would check the default language and know which message to display
       String text = "";
       String dele= "0";
       List<labels> labels = labelRepository.findALL("0");
       List<Ussdfirstpage> menu = ussdfirstpageRepository.findAllActive();
       List<Ussdfirstpage> sortedmenu = menu.stream().sorted(Comparator.comparing(Ussdfirstpage::getRang)).collect(Collectors.toList());
     if(message.equalsIgnoreCase("2")) {
         map.put("message", "Votres solde c'est : 5 000 000 FCFA");
         map.put("command", 1);
     } else {
         // if(!Objects.isNull(user) && user.getDele().equalsIgnoreCase("0") && ( !Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){

         String menu_elements = this.getValueByKey("menu_head", labels)[1];
         for (Ussdfirstpage elements : sortedmenu) {
             int va = 1 + elements.getRang();
             menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
         }

         text = menu_elements;
         map.put("message", text);
         map.put("command", 1);
         // }
     }

         return ResponseEntity.status(HttpStatus.OK).body(map.toString());
        //return map;
    }
     @RequestMapping(value = "/endpoint/{msisdn}/{sessionid}/{message}/{provider}" , method =  RequestMethod.GET)
    ResponseEntity<String> enpoint1(@PathVariable String msisdn,@PathVariable String sessionid,@PathVariable String message,@PathVariable String provider) {
        System.out.println("yvo login Test3 de USSD:  "+provider);
        //String msisdn=checkPayload(payload, "msisdn").toString();
       // String sessionid= checkPayload(payload, "sessionid").toString();
       // String message=checkPayload(payload, "message").toString();
        //String provider=checkPayload(payload, "provider").toString();
         JSONObject map = new JSONObject();
         String newLine = System.getProperty("line.separator");
         String send="Welcome to First Trust" + newLine +" 2: Balance "+ newLine +" 3: Purchase "+ newLine +" 4: Statement "+ newLine +" 5: Enquiry "+ newLine +" 0: Exit";

         map.put("message", send);
         map.put("command", "1");
        return ResponseEntity.status(HttpStatus.OK).body(map.toString());
    }
     @RequestMapping(value = "/endpoint2/{provider}" , method =  RequestMethod.GET)
    public  Map<String, String> enpoint2(@PathVariable String provider) {
        System.out.println("yvo login Test3 de USSD:  "+provider);
        //String msisdn=checkPayload(payload, "msisdn").toString();
       // String sessionid= checkPayload(payload, "sessionid").toString();
       // String message=checkPayload(payload, "message").toString();
        //String provider=checkPayload(payload, "provider").toString();
         Map<String, String> map = new HashMap();
         String newLine = System.getProperty("line.separator");
         String send="Welcome to First Trust" + newLine +" 2: Balance "+ newLine +" 3: Purchase "+ newLine +" 4: Statement "+ newLine +" 5: Enquiry "+ newLine +" 0: Exit";
//          String send="Welcome to First Trust"
//                  + "Choisissez une option"
//                  + "1: Balance"
//                  + "2: Purchase"
//                  + "3: Statement"
//                  + "4: Enquiry"
//                  + "0: Exit";
         map.put("message", send);
         map.put("command", "1");
        return map;
    }

    public String getInvalAccounts(String tel){
        Map<String, String> request = new HashMap();
        request.put("tel", tel);
        request.put("etab", "001");
        String url = digitalUrl+"/getCptInvAttachU";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response  = null;
        try{
            response = restTemplate.postForEntity(url, entity, String.class);
        }
        catch(HttpStatusCodeException ex){
            System.out.println("yann see this ex "+ex.getMessage());
            System.out.println("Yankoo see this bro "+ ResponseEntity.status(ex.getRawStatusCode()).headers(ex.getResponseHeaders()).body(ex.getResponseBodyAsString()));
            
        }
        JSONObject result = new JSONObject();
        System.out.println("yann see this res string "+ response.toString());
        
        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            if(res.get("success").toString().equalsIgnoreCase("01")){
                return res.get("data").toString();
            }
            else{
                return "fail";
            }
        }
        
        return "error";
    }
    
    public String[] getValueByKey(String key, List<labels> labels){
        String[] val = new String[3];
        val[0] = null;
        val[1] = null;
        
        for (labels lable : labels){
            if(lable.getKey().equalsIgnoreCase(key)){
                val[0] = lable.getValen();
                val[1] = lable.getValfr();
            }
        }
        
        return val;
        
    }
    
    public Map<String, Object> checkPayload(Map<String, Object> payload, String key) {
        if (!payload.containsKey(key)) {
            payload.put(key, "");
        }
        return payload;
    }
    
}
