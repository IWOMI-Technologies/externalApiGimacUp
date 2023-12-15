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
import com.iwomi.External_api_cccNewUp.ussd.service.UssdService;
import com.iwomi.External_api_cccNewUp.ussd.utils.ManageUssdPosition;
import com.iwomi.External_api_cccNewUp.ussd.utils.MessageUssdUtils;
import com.iwomi.External_api_cccNewUp.ussd.utils.UtilsUssd;
import com.iwomi.External_api_cccNewUp.ussd.utils.UtilsUssdscpg;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import io.micrometer.core.annotation.Timed;

/**
 *
 * @author user
 */
@RestController

public class USSDController
{
    
    @Value("${digitalbackoffice.url}")
    private String digitalUrl;
    
    @Autowired
    ManageUssdPosition manageuserposition;
    
    @Autowired
    LabelRepository labelRepository;
    
    @Autowired
    UssdfirstpageRepository ussdfirstpageRepository;
    
    @Autowired
    UssdService ussd;
    @Autowired
    UserSessionRepo usersRepo;
    @Autowired
    MessageSource messageSource;
    @RequestMapping(value = { "/endpointCCC" }, method = { RequestMethod.POST })
    @Timed
    @Transactional(timeout = 1200)
    public String MyService(@RequestBody final String MTNNotifications, final HttpServletRequest request, final HttpServletResponse response) {
        
        /*
        i would get all the config attributes for the labels
        
        */
        
        List<labels> labels = labelRepository.findALL("0");
        
        
        final boolean beginRoolback = false;
        try {
            final String phone = UtilsUssd.getFullNameFromXml(MTNNotifications, TemplatesUSSD.getTagMsdnText()).get(0);
            final String userString = UtilsUssd.getFullNameFromXml(MTNNotifications, TemplatesUSSD.getTagTexteString()).get(0);
            if (!Objects.isNull(phone) && !phone.trim().equalsIgnoreCase(TemplatesUSSD.getEmptyString())) {
                
                //final List<Ussdfirstpage> list = usdfirstpgerepo.findAllActive();
                /*
                for now i would put my firstpage static, which would be configurable later
                */
                //this is the first menu, if the user hasn't select a language
                String text = "";
                String dele= "0";
                
                final UserSession user = usersRepo.findClientByPhone(this.getPhone(phone)).orElse(null);
                // would check the default language and know which message to display
                
                List<Ussdfirstpage> menu = ussdfirstpageRepository.findAllActive();
                List<Ussdfirstpage> sortedmenu = menu.stream().sorted(Comparator.comparing(Ussdfirstpage::getRang)).collect(Collectors.toList());
                      
                if(!Objects.isNull(user) && user.getDele().equalsIgnoreCase("0") && ( !Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){
                    //Send the first menu if french
                    /*text = "Bienvenue chez CCC PLC \nChoisis une option"
                        + "\n 1 : Consulter Solde"
                        + "\n 2 : Mini releve"
                        + "\n 3 : Bank to MOMO"
                        + "\n 4 : MOMO to Bank"
                        + "\n 5 : Abonnez vous/desinscrire"
                        + "\n 6 : Changer PIN"
                        + "\n 7 : Change Language to English"
                        + "\n 99 :Quitter" ;
                    */     
                    
                    String menu_elements = this.getValueByKey("menu_head", labels)[1];
                    for(Ussdfirstpage elements: sortedmenu){
                        menu_elements = menu_elements + "\n"+elements.getRang() + " : "+elements.getValfr();
                    }
                    
                    text = menu_elements;
                    
                }
                else if(!Objects.isNull(user) && user.getDele().equalsIgnoreCase("0") && (Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en")))){
                    //Send the first menu if English
                    /*text = "Welcome to CCC PLC \nSelect an option"
                        + "\n 1 : Consult Balance"
                        + "\n 2 : Mini Statement"
                        + "\n 3 : Bank to MOMO"
                        + "\n 4 : MOMO to Bank"
                        + "\n 5 : Subscribe/Unsubscribe"
                        + "\n 6 : Change PIN"
                        + "\n 7 : Changer la langue en francais"
                        + "\n 99 :Exit";
                    */
                    String menu_elements = this.getValueByKey("menu_head", labels)[0];
                    for(Ussdfirstpage elements: sortedmenu){
                        menu_elements = menu_elements + "\n"+elements.getRang() + " : "+elements.getValen();
                    }
                    text = menu_elements;
                }
                else if(!Objects.isNull(user) && user.getStatus().equalsIgnoreCase("2") ){
                    if( Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                        //Tell him in english that is account has been deactivated, let him go the his branch
                        //text = "Account has been blocked, kindly approach your CCC PLC Branch to activate your account";
                        text = this.getValueByKey("blocked_account", labels)[0];
                    }
                    else{
                        //tell him deactivate account in french
                        //text = "Le compte a ete bloque, veuillez vous adresser a votre Agence CCC PLC pour activer votre compte";
                        text = this.getValueByKey("blocked_account", labels)[1];
                    }
                    //Send the first menu if English
                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MTNNotifications, phone, text);
                }
                else{
                    //Abort, user is not subscribe, ask him or her to present herself to her branch office
                    //text = "You are not subscribed to this service, kindly approach your CCC PLC Branch to get subscribe";
                    text = this.getValueByKey("not_subscribe", labels)[0];
                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MTNNotifications, phone, text);
                    
                }
                
                System.out.println(" yann see this userstring "+userString);
                
                if (userString.startsWith(UssdConfigFile.getUserShortCode())) {
                    //here is a new session, first page
                    if (Objects.isNull(user)) {
                        SendUssdUtils.generateStartSMSUssd(MTNNotifications, phone, text);
                    }
                    /*
                    this is the firstpage, i would compare the user object if it exist
                    and status  
                    0 = validate Acccount attachment with OTP, 
                    1 = level of change password, 2= account is blocked ,
                    3 = account is normal, evreything ok
        
                    */
                    if (!Objects.isNull(user) && user.getStatus().equalsIgnoreCase("0")) {
                        //Validate Account attachment
                        /*
                        i would get the list of account and present, then request OTP code send to user
                        we have phone, we would call the services to get list of account
                        */
                        
                        
                        
                        String status = this.getInvalAccounts(phone);
                        if(status.equalsIgnoreCase("error") || status.equalsIgnoreCase("fail")){
                            // return error
                            String err = this.getValueByKey("error_occured", labels)[0];
                            SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MTNNotifications, phone, /*"An Error occured, please try again later"*/err);
                            
                        }
                        else{
                            String accounts = "";
                            try{
                                JSONArray acc = new JSONArray(status);
                                user.setAcc(status);
                                for(int i=0; i<acc.length(); i++){
                                    accounts = accounts + "\n"+new Double(i+1).longValue() +" : "+acc.getJSONObject(i).getString("cpt");
                                    
                                    System.out.println(accounts);
                                }
                                
                                
                                if( Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                    //confirm account attachement with OTP send you to your number via SMS
//                                    text = "Enter OTP send to your number via SMS to confirm account Attachment."
//                                            + accounts;
                                    text = this.getValueByKey("enter_otp", labels)[0]+ accounts;
                                }
                                else{
                                    //tell him deactivate account in french
//                                    text = "Entrez OTP envoyer a votre numero par SMS pour confirmer l'attachement de votre compte"
//                                            + accounts;
                                    text = this.getValueByKey("enter_otp", labels)[1]+ accounts;
                                }
                                user.setPos(TemplatesUSSD.getAttchUserPositionOnStringUssd());//attch positoin *atch
                                SendUssdUtils.generateStartSMSUssd(MTNNotifications, phone, text);
                                usersRepo.saveAndFlush(user);
                                
                            }
                            catch(Exception ex){
                                // return an error occured
//                                String err  = this.getValueByKey("error_occured", labels)[0];
//                                SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MTNNotifications, phone, /*"An Error occured, please try again later"*/err);
                                String err = this.getValueByKey("error_occured", labels)[0];
                                SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MTNNotifications, phone, /*"An Error occured, please try again later"*/err);
                            
                            }
                            
                            
                           
                            
                            
                        }

                        
                    }
                    //account requires change of PIN yanick
                    /*
                    you have to implement change of PIN
                    */
                    else if (!Objects.isNull(user) && user.getStatus().equalsIgnoreCase("1")) {
                        String text_gen = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //text_gen = "Veuillez entrer votre ancien code PIN";
                            text_gen = this.getValueByKey("enter_oldpin", labels)[0];
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //text_gen = "Please Enter your OLD PIN";
                            text_gen = this.getValueByKey("enter_oldpin", labels)[1];
                        }
                        user.setPos(TemplatesUSSD.getAttchUserPositionOnStringUssd()+ TemplatesUSSD.getBaseSeparateurChoice() + "123456" + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                        SendUssdUtils.generateStartSMSUssd(MTNNotifications, phone, text_gen);
                        usersRepo.saveAndFlush(user);
                    }
                    /*
                    Account is blocked, go to your branch to unblock it
                    */
                    else if (!Objects.isNull(user) && user.getStatus().equalsIgnoreCase("2")) {
                        String account_blocked = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //account_blocked = "Votre compte a ete bloque.\n Rapprochez-vous de votre Agence CCC PLC pour debloquer votre compte";
                            account_blocked = this.getValueByKey("blocked_account", labels)[0];
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //account_blocked = "Your Account has been blocked.\n Approach your CCC PLC Branch to unblock your Account";
                            account_blocked = this.getValueByKey("blocked_account", labels)[1];
                        }

                        user.setPos(null);
                        usersRepo.saveAndFlush(user);
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MTNNotifications, phone, account_blocked);

                    }
                    
                    else if (!Objects.isNull(user) && user.getStatus().equalsIgnoreCase("4")) {
                        String account_blocked = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //accounts awaits agency validation and you would receive a PIN code
                            account_blocked = this.getValueByKey("agency_val_msg", labels)[0];
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //accounts awaits agency validation and you would receive a PIN code
                            account_blocked = this.getValueByKey("agency_val_msg", labels)[1];
                        }

                        user.setPos(null);
                        usersRepo.saveAndFlush(user);
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MTNNotifications, phone, account_blocked);

                    }
                    
                    else {
                        user.setPos(TemplatesUSSD.getBaseUserPositionOnStringUssd());//base positoin *init
                        if (!beginRoolback) {
                            user.setIscon(false);
                            SendUssdUtils.generateStartSMSUssd(MTNNotifications, phone, text);
                        }
                        else {
                            SendUssdUtils.generateStartSMSUssdRollBack(MTNNotifications, phone, text);
                        }
                        usersRepo.saveAndFlush(user);
                    }
                }
                else if (UtilsUssdscpg.isInt(userString)) {
                    // here us a response to continue
                    System.out.println("yann am inside here");
                    if (Objects.isNull(user)) {
                        SendUssdUtils.generateNotUserSMSUssd(MTNNotifications, phone, MessageUssdUtils.NotUSerMessage(phone));
                    }
                    else {
                        // i would tried to change the page and
                        /*
                        We would get the user postion first, if()
                        */
                    	manageuserposition.manageUserPosition(user, phone, MTNNotifications, messageSource,labels);
                    	
                    }
                }
                else {
                    SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MTNNotifications, phone, MessageUssdUtils.wrongUserSelected(phone, TemplatesUSSD.getBaseLocaleString(), messageSource));
                }
            }
            else {
                if (MTNNotifications.toLowerCase().contains(TemplatesUSSD.getNotificationabort().toLowerCase())) {
                    System.out.println("Abort USSD");
                    return UtilsUssd.getResponseAbortNotifyUSSD();
                }
                if (MTNNotifications.toLowerCase().contains(TemplatesUSSD.getSendUssdAbortString().toLowerCase())) {
                    System.out.println("Abort Send USSD");
                    return UtilsUssd.getResponseSendUssdAbort();
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return UtilsUssd.getResponseReceiveUSSDDemand();
    }
    
    @RequestMapping(value = { "/startussdnotif" }, method = { RequestMethod.GET })
    @Timed
    @Transactional(timeout = 1200)
    public ResponseEntity<?> initiateUSSDNotificationManager() {
        try {
            final String timestampString = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
            final String spid = UssdConfigFile.getSPID_USSD();
            return (ResponseEntity<?>)ussd.startUSSDManager(spid, timestampString, UtilsUssd.generateMD5(spid, UssdConfigFile.getSPPassword_USSD(), timestampString), UssdConfigFile.getServiceID_USSD(), UssdConfigFile.getEndPointUssdNOTIFICATION(), UssdConfigFile.getCorrelatorStartUSSDAndStopUSSD(), UssdConfigFile.getUserShortCode());
        }
        catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses("Error on Start USSD Manager Notification"));
        }
    }
    
    public String getPhone(final String phone) {
        if (phone.startsWith("+237")) {
           // return phone.replaceFirst("+237", "");
            return phone.replaceFirst("237", "");
        }
        if (phone.startsWith("237")) {
            return phone.replaceFirst("237", "");
        }
        return phone;
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
    
//    @RequestMapping(value = "/gethead",method=RequestMethod.GET)
//    public String getMenu(){
//        
//        
//        List<labels> labels = labelRepository.findALL("0");
//        List<Ussdfirstpage> menu = ussdfirstpageRepository.findAllActive();
//        List<Ussdfirstpage> sortedmenu = menu.stream().sorted(Comparator.comparing(Ussdfirstpage::getRang)).collect(Collectors.toList());
//        
//        String menu_elements = this.getValueByKey("menu_head", labels)[1];
//        for(Ussdfirstpage elements: sortedmenu){
//            menu_elements = menu_elements + "\n"+elements.getRang() + " : "+elements.getValfr();
//        }
//        
//        return menu_elements;
//    }
    
}

