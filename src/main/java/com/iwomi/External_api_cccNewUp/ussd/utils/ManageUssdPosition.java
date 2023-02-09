/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.utils;

import com.iwomi.External_api_cccNewUp.model.UserSession;
import com.iwomi.External_api_cccNewUp.model.Ussdfirstpage;
import com.iwomi.External_api_cccNewUp.model.labels;
import com.iwomi.External_api_cccNewUp.repository.UserSessionRepo;
import com.iwomi.External_api_cccNewUp.repository. UssdfirstpageRepository ;
import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import com.iwomi.External_api_cccNewUp.ussd.service.SendUssdUtils;
import com.iwomi.External_api_cccNewUp.ussd.service.UssdService;

import java.util.Objects;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
/**
 *
 * @author user
 */
@Component
public class ManageUssdPosition
{
    @Autowired
    UserSessionRepo userRepo;
    
    @Autowired
    UssdService ussdService;
    
    @Autowired
    UssdfirstpageRepository ussdfirstpageRepository;
    
    
    @Value("${digitalbackoffice.url}")
    private String digitalUrl;
    public  String rollBackMenu(final UserSession user, final MessageSource messagessource) {
        return "";
    }
    
    public void manageUserPosition(final UserSession user, final String phone, final String MtnNotifications, final MessageSource messageSource, List<labels> labels) throws UnsupportedEncodingException {
        
        /*
        i would start check if its account attachment instead
        
        */
        
        int screen_number = StringUtils.countMatches(user.getPos(), TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
       
        System.out.println("Yann see this screen number out : "+screen_number);
        System.out.println("Yann pos out : "+user.getPos());
        if(user.getPos().equalsIgnoreCase(TemplatesUSSD.getAttchUserPositionOnStringUssd())){
            //this is the OTP that has been entered for validation
             System.out.println(" i am here oooh see me");
            final String userchoice = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
            System.out.println(" userchoice "+userchoice);
            if (UtilsUssdscpg.isInt(userchoice)) {
                String opt = userchoice;
                //phone
                /*
                normally we would call the services to check if Opt and phone is valid
                */
                
                /*
                check otp
                
                */
                String age = new JSONArray(user.getAcc()).getJSONObject(0).getString("age");
                String cpt =  new JSONArray(user.getAcc()).getJSONObject(0).getString("cpt");
                String stat = this.checkOtpAccountAttachment(phone, opt, age, cpt);
                if( stat.equalsIgnoreCase("error")){
                        
                    // return error
                    String err = this.getValueByKey("error_occured", labels)[0];
                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                       
                    
                }

                else if(stat.equalsIgnoreCase("success")){
                    
                    if(user.getStatus().equalsIgnoreCase("1")){
                        /*
                        change PIN
                        */
                        /*
                        i would confirm that it's correct and ask him to wait come, enter his new PIN
                        */
                        String text_gen = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //text_gen = "Veuillez entrer votre ancien code PIN";
                            text_gen = this.getValueByKey("enter_oldpin", labels)[1];

                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //text_gen = "Please Enter your OLD PIN";
                            text_gen = this.getValueByKey("enter_oldpin", labels)[0];

                        }
                        user.setLib2(userchoice);

                        user.setPreval(userchoice);
                        user.setPos(user.getPos() + TemplatesUSSD.getBaseSeparateurChoice() + userchoice + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                        userRepo.saveAndFlush(user);
                        //we request the PIN and proceed tolist the accounts
                        SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);
                        
                    }
                    else{
                        
                        String invalid = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //invalid = "Succès ! Votre compte est en cours de validation, nous vous enverrons votre code PIN par défaut sous peu.\nMerci de nous faire confiance";
                            invalid = this.getValueByKey("attah_acc_success", labels)[1];
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //invalid = "Success!your account is under validation, we would send your default PIN via SMS shortly.\nThanks for trusting us";
                            invalid = this.getValueByKey("attah_acc_success", labels)[0];
                        }
                        
                        user.setStatus("4");
                        user.setPos(null);
                        userRepo.saveAndFlush(user);
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);
                        
                    }
                    
                    
                    
                }
                else{
                    if(user.getNwotp()==0){
                        String invalid = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //invalid = "Contactez votre agence pour obtenir un nouveau code OTP.\nMerci de nous faire confiance";
                            invalid = this.getValueByKey("otp_code_block", labels)[1];
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //invalid = "Contact your branch to get a new OTP Code.\nThanks for trusting us";
                            invalid = this.getValueByKey("otp_code_block", labels)[0];
                        }

                        user.setPos(null);
                        userRepo.saveAndFlush(user);
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);

                    }

                    String text_gen = "";
                    if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                        //text_gen = "Code OTP incorrect, veuillez reessayer.\n"+(user.getNwotp()-1)+ " essais restants";
                        text_gen = this.getValueByKey("otp_reenter", labels)[1];

                    }
                    else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                        //text_gen = "OTP Code incorrect, Please try again.\n"+(user.getNwotp()-1)+ " Trials remaining";
                        text_gen = this.getValueByKey("otp_reenter", labels)[0];
                    }
                    user.setInval(user.getNwotp()-1);

                    user.setPreval(userchoice);
                    userRepo.saveAndFlush(user);
                    //we request the PIN and proceed tolist the accounts
                    SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);
                
                    
                }
            
            }
            else{
                //Didn't entered an integer value, error
                //Invalid Data type, we would ass user to re-enter correct character
                if(user.getInval()==0){
                    String invalid = "";
                    if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                        //invalid = "Reponse invalide.\nMerci de nous faire confiance";
                        invalid = this.getValueByKey("invalid_reply", labels)[1];

                    }
                    else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                        //invalid = "Invalid Response.\nThanks for trusting us";
                        invalid = this.getValueByKey("invalid_reply", labels)[0];

                    }

                    user.setPos(null);
                    userRepo.saveAndFlush(user);
                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);
                    
                }
                
                String text_gen = "";
                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                    //text_gen = "Format incorrect, veuillez entrer un code a 4 chiffres";
                    text_gen = this.getValueByKey("invalid_format_otp", labels)[1];

                }
                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                    //text_gen = "Incorrect format, Please Enter a 4 digit number";
                    text_gen = this.getValueByKey("invalid_format_otp", labels)[0];

                }
                user.setInval(user.getInval()-1);

                user.setPreval(userchoice);
                userRepo.saveAndFlush(user);
                //we request the PIN and proceed tolist the accounts
                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);
                
            
                
            }
            
            
            
        }
        /*
        i would work with new pin and confirm here
        
        */
        
        else if(user.getPos().contains(TemplatesUSSD.getAttchUserPositionOnStringUssd()) && screen_number==1){
            //this is the OTP that has been entered for validation
             System.out.println(" i am here oooh see me");
            final String userchoice = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
            System.out.println(" userchoice "+userchoice);
            if (UtilsUssdscpg.isInt(userchoice) ) {
                String newpin1 = userchoice;
                //phone
                /*
                We would ask for confirmation
                */
                String text_gen = "";
                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                    //text_gen = "Veuillez entrer un nouveau code PIN";
                    text_gen = this.getValueByKey("enter_newpin", labels)[1];

                }
                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                    //text_gen = "Please Enter a new PIN";
                    text_gen = this.getValueByKey("enter_newpin", labels)[0];
                }
                
                user.setLib2(userchoice);//this is the old PIN

                user.setPreval(userchoice);
                user.setPos(user.getPos() + TemplatesUSSD.getBaseSeparateurChoice() + userchoice + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                userRepo.saveAndFlush(user);
                //we request the PIN and proceed tolist the accounts
                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);
                
            
            }
            else{
                //Didn't entered an integer value, error
                //Invalid Data type, we would ass user to re-enter correct character
                if(user.getInval()==0){
                    String invalid = "";
                    if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                        //invalid = "Reponse invalide.\nMerci de nous faire confiance";
                        invalid = this.getValueByKey("invalid_reply", labels)[1];

                    }
                    else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                        //invalid = "Invalid Response.\nThanks for trusting us";
                        invalid = this.getValueByKey("invalid_reply", labels)[0];
                    }

                    user.setPos(null);
                    userRepo.saveAndFlush(user);
                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);
                    
                }
                
                String text_gen = "";
                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                    //text_gen = "Format incorrect, veuillez entrer un code a 6 chiffres";
                    text_gen = this.getValueByKey("invalid_format_pin", labels)[1];

                }
                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                    //text_gen = "Incorrect format, Please Enter a 6 digit number";
                    text_gen = this.getValueByKey("invalid_format_pin", labels)[0];
                }
                user.setInval(user.getInval()-1);

                //user.setPreval(userchoice);
                userRepo.saveAndFlush(user);
                //we request the PIN and proceed tolist the accounts
                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);
                
            
                
            }
            
            
            
        }
        
        else if(user.getPos().contains(TemplatesUSSD.getAttchUserPositionOnStringUssd()) && screen_number==2){
            //this is the OTP that has been entered for validation
             System.out.println(" i am here oooh see me");
            final String userchoice = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
            System.out.println(" userchoice "+userchoice);
            if (UtilsUssdscpg.isInt(userchoice) && userchoice.length()==6) {
                String newpin1 = userchoice;
                //phone
                /*
                We would ask for confirmation
                */
                String text_gen = "";
                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                    //text_gen = "Veuillez confirmer votre nouveau code PIN";
                    text_gen = this.getValueByKey("confirm_newpin", labels)[1];

                }
                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                    //text_gen = "Please confirm your new PIN";
                    text_gen = this.getValueByKey("confirm_newpin", labels)[0];

                }
                
                

                user.setPreval(userchoice);
                user.setPos(user.getPos() + TemplatesUSSD.getBaseSeparateurChoice() + userchoice + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                userRepo.saveAndFlush(user);
                //we request the PIN and proceed tolist the accounts
                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);
                
            
            }
            else{
                //Didn't entered an integer value, error
                //Invalid Data type, we would ass user to re-enter correct character
                if(user.getInval()==0){
                    String invalid = "";
                    if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                        //invalid = "Reponse invalide.\nMerci de nous faire confiance";
                        invalid = this.getValueByKey("invalid_reply", labels)[1];
                    }
                    else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                        //invalid = "Invalid Response.\nThanks for trusting us";
                        invalid = this.getValueByKey("invalid_reply", labels)[0];
                    }

                    user.setPos(null);
                    userRepo.saveAndFlush(user);
                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);
                    
                }
                
                String text_gen = "";
                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                    //text_gen = "Format incorrect, veuillez entrer un code a 6 chiffres";
                    text_gen = this.getValueByKey("invalid_format_pin", labels)[1];

                }
                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                    //text_gen = "Incorrect format, Please Enter a 6 digit number";
                    text_gen = this.getValueByKey("invalid_format_pin", labels)[0];

                }
                user.setInval(user.getInval()-1);

                //user.setPreval(userchoice);
                userRepo.saveAndFlush(user);
                //we request the PIN and proceed tolist the accounts
                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);
                
            
                
            }
            
            
            
        }
        
        else if(user.getPos().contains(TemplatesUSSD.getAttchUserPositionOnStringUssd()) && screen_number==3){
            //this is the OTP that has been entered for validation
             System.out.println(" i am here oooh see me");
            final String userchoice = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
            System.out.println(" userchoice "+userchoice);
            if (UtilsUssdscpg.isInt(userchoice) && userchoice.length()==6 ) {
                String newpin1 = userchoice;
                //phone
                /*
                We would ask for confirmation
                */
                if(!userchoice.equalsIgnoreCase(user.getPreval())){
                    
                    if(user.getInval()==0){
                        String invalid = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //invalid = "Reponse invalide.\nMerci de nous faire confiance";
                            invalid = this.getValueByKey("invalid_reply", labels)[1];
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //invalid = "Invalid Response.\nThanks for trusting us";
                            invalid = this.getValueByKey("invalid_reply", labels)[0];
                        }

                        user.setPos(null);
                        userRepo.saveAndFlush(user);
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);

                    }

                    String text_gen = "";
                    if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                        //text_gen = "Le nouveau code PIN ne correspond pas, veuillez confirmer a nouveau le code PIN";
                        text_gen = this.getValueByKey("newpin_notmatch", labels)[1];

                    }
                    else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                        //text_gen = "New PIN does not match, please confirm PIN again";
                        text_gen = this.getValueByKey("newpin_notmatch", labels)[0];

                    }
                    user.setInval(user.getInval()-1);

                    //user.setPreval(userchoice);
                    userRepo.saveAndFlush(user);
                    //we request the PIN and proceed tolist the accounts
                    SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);

                    
                    
                }
                else{
                    /*
                    change password here
                    */
                    String oldpin = user.getLib2();
                    String newpin = userchoice;
                    /*
                    i would call the webservice and update the PIN

                    */
                    String invalid = "";
                    String stat = this.changePIN(phone, oldpin,newpin);
                    if( stat.equalsIgnoreCase("error")){
                        // return error
                        // return error
                        String err = this.getValueByKey("error_occured", labels)[0];
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                       
                    }

                    else if(stat.equalsIgnoreCase("success")){
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //invalid = "PIN a ete change avec succès.\nMerci de nous faire confiance";
                            invalid = this.getValueByKey("changepin_success", labels)[1];

                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //invalid = "PIN Successfully updated.\nThanks for trusting us";
                            invalid = this.getValueByKey("changepin_success", labels)[0];

                        }
                        user.setStatus("3");

                    }
                    else{
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //invalid = "Mauvais code PIN entré.\nMerci de nous faire confiance";
                            invalid = this.getValueByKey("wrongpin_msg", labels)[1];

                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //invalid = "Wrong PIN entered.\nThanks for trusting us";
                            invalid = this.getValueByKey("wrongpin_msg", labels)[0];

                        }

                    }

                    user.setPos(null);
                    userRepo.saveAndFlush(user);
                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);



                }
                
                
            
            }
            else{
                //Didn't entered an integer value, error
                //Invalid Data type, we would ass user to re-enter correct character
                if(user.getInval()==0){
                    String invalid = "";
                    if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                        //invalid = "Reponse invalide.\nMerci de nous faire confiance";
                        invalid = this.getValueByKey("invalid_reply", labels)[1];

                    }
                    else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                        //invalid = "Invalid Response.\nThanks for trusting us";
                        invalid = this.getValueByKey("invalid_reply", labels)[0];

                    }

                    user.setPos(null);
                    userRepo.saveAndFlush(user);
                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);
                    
                }
                
                String text_gen = "";
                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                    //text_gen = "Format incorrect, veuillez entrer un code a 6 chiffres";
                    text_gen = this.getValueByKey("invalid_format_pin", labels)[1];

                }
                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                    //text_gen = "Incorrect format, Please Enter a 6 digit number";
                    text_gen = this.getValueByKey("invalid_format_pin", labels)[0];

                }
                user.setInval(user.getInval()-1);

                //user.setPreval(userchoice);
                userRepo.saveAndFlush(user);
                //we request the PIN and proceed tolist the accounts
                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);
                
            
                
            }
            
            
            
        }
        
        
        
        else{


            if(screen_number ==  0){
                System.out.println("Yann see this screen number: "+screen_number);
                //second screen
                /****
                 * This is after the first page, that is *init
                 */

                System.out.println(" i am here oooh see me");
                final String userchoice = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
                System.out.println(" userchoice "+userchoice);
                if (UtilsUssdscpg.isInt(userchoice)) {


                    System.out.println(" i am here oooh see me 1");
                    /*
                    This here i would do a switch and redirect to different pages

                    */
                    // send second menu
                    /*
                    i would test if english or french
                    */
                    
                    /*
                    i would test if services isn't desactivate inorder to response invalid
                    
                    */
                    String active = "1";
                    Ussdfirstpage menu_items = ussdfirstpageRepository.findByRang(userchoice, active);
                    if(menu_items == null){
                        //we would exit here
                        String text_gen2 = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //text_gen2 = "Reponse invalide.\nMerci de nous faire confiance";
                            text_gen2 = this.getValueByKey("invalid_reply", labels)[1];

                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //text_gen2 = "Invalid Response.\nThanks for trusting us";
                            text_gen2 = this.getValueByKey("invalid_reply", labels)[0];

                        }

                        user.setPos(null);
                        userRepo.saveAndFlush(user);
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, text_gen2);
                    }
                    else
                    {
                        switch (userchoice) {
                            case "1":
                            case "2":
                            case "3":
                            case "4":

                                String text_gen = "";
                                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                    //text_gen = "Veuillez entrer votre code PIN";
                                    text_gen = this.getValueByKey("enter_pin", labels)[1];

                                }
                                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                    //text_gen = "Please Enter your PIN";
                                    text_gen = this.getValueByKey("enter_pin", labels)[0];

                                }

                                user.setPreval(userchoice);
                                user.setPos(user.getPos() + TemplatesUSSD.getBaseSeparateurChoice() + userchoice + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                                userRepo.saveAndFlush(user);
                                //we request the PIN and proceed tolist the accounts
                                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);

                                break;
                                /*
                            case "5":
                                //this is to subscribeor unsubscribe
                                String invalid = "";
                                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                    //invalid = "Veuillez vous rapprocher de votre Agence CCC PLC pour vous abonner ou vous desabonner de ce service.\nMerci de nous faire confiance";
                                    invalid = this.getValueByKey("nor_subscribe", labels)[1];
                                }
                                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                    //invalid = "Kindly approach your CCC PLC branch to subscribe or unsubscribe for this service.\nThanks for trusting us";
                                    invalid = this.getValueByKey("nor_subscribe", labels)[0];
                                }
                                user.setPos(null);
                                userRepo.saveAndFlush(user);
                                SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);

                                break;
                                */
                            case "5":
                                //This is to change the PIN
                                String text_change_pin = "";
                                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                    //text_change_pin = "Veuillez entrer votre ancien code PIN";
                                    text_change_pin = this.getValueByKey("enter_oldpin", labels)[1];

                                }
                                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                    //text_change_pin = "Please Enter your OLD PIN";
                                    text_change_pin = this.getValueByKey("enter_oldpin", labels)[0];

                                }
                                user.setPos(TemplatesUSSD.getAttchUserPositionOnStringUssd()+ TemplatesUSSD.getBaseSeparateurChoice() + "123456" + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_change_pin);
                                userRepo.saveAndFlush(user);

                                break;
                            case "6":
                                String text = "";
                                List<Ussdfirstpage> menu = ussdfirstpageRepository.findAllActive();
                                List<Ussdfirstpage> sortedmenu = menu.stream().sorted(Comparator.comparing(Ussdfirstpage::getRang)).collect(Collectors.toList());
                 
                                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en")){
                                    //Send the first menu if french
//                                    text = "Bienvenue chez CCC PLC \nChoisis une option"
//                                        + "\n 1 : Consulter Solde"
//                                        + "\n 2 : Mini releve"
//                                        + "\n 3 : Bank to MOMO"
//                                        + "\n 4 : MOMO to Bank"
//                                        + "\n 5 : Abonnez vous/desinscrire"
//                                        + "\n 6 : Changer PIN"
//                                        + "\n 7 : Change Language to English"
//                                        + "\n 99 :Quitter" ;

                                    String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                    for(Ussdfirstpage elements: sortedmenu){
                                        menu_elements = menu_elements + "\n"+elements.getRang() + " : "+elements.getValfr();
                                    }

                                    text = menu_elements;
                                    user.setLang("fr");
                                }
                                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){
                                    //Send the first menu if English
//                                    text = "Welcome to CCC PLC \nSelect an option"
//                                        + "\n 1 : Consult Balance"
//                                        + "\n 2 : Mini Statement"
//                                        + "\n 3 : Bank to MOMO"
//                                        + "\n 4 : MOMO to Bank"
//                                        + "\n 5 : Subscribe/Unsubscribe"
//                                        + "\n 6 : Change PIN"
//                                        + "\n 7 : Changer la langue en francais"
//                                        + "\n 99 :Exit";
                                    String menu_elements = this.getValueByKey("menu_head", labels)[0];
                                    for(Ussdfirstpage elements: sortedmenu){
                                        menu_elements = menu_elements + "\n"+elements.getRang() + " : "+elements.getValen();
                                    }
                                    text = menu_elements;
                                    user.setLang("en");
                                }

                                //Change default langauge
                                user.setPos(TemplatesUSSD.getBaseUserPositionOnStringUssd());//base positoin *init
                                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text);
                                userRepo.saveAndFlush(user);

                                break;
                            case "99":
                                //exiting
                                String text_gen1 = "";
                                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                    //text_gen1 = "Merci de nous faire confiance";
                                    text_gen1 = this.getValueByKey("exit_msg", labels)[1];

                                }
                                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                    //text_gen1 = "Thank you for trusting us";
                                    text_gen1 = this.getValueByKey("exit_msg", labels)[0];

                                }

                                user.setPos(null);
                                userRepo.saveAndFlush(user);
                                SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, text_gen1);

                                break;
                            default:

                                String text_gen2 = "";
                                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                    //text_gen2 = "Reponse invalide.\nMerci de nous faire confiance";
                                    text_gen2 = this.getValueByKey("invalid_reply", labels)[1];

                                }
                                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                    //text_gen2 = "Invalid Response.\nThanks for trusting us";
                                    text_gen2 = this.getValueByKey("invalid_reply", labels)[0];

                                }

                                user.setPos(null);
                                userRepo.saveAndFlush(user);
                                SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, text_gen2);

                            }
                    
                        
                        }

                    // switch statement with int data type
                        

                    }

                    else {
                        SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MtnNotifications, phone, "Invalid reply");
                    }
            }
            else if(screen_number ==  1){
                System.out.println("Yann see this screen number: "+screen_number);
                //This is the third page after the PIN has been entered, we would
                /*
                I would get the PIN, test if it's correct or not, if wrong, ask the user to resend the PIN and maintain same Screen
                Else i would list the account options
                */
                 System.out.println(" i am here oooh see me");
                final String userchoice = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
                System.out.println(" userchoice "+userchoice);
                if (UtilsUssdscpg.isInt(userchoice) && userchoice.length()==6) {
                    user.setInval(2);
                    String pin = userchoice;
                    /*
                    use the phone and PIN to check if its correct
                    using webservices
                    */
                    
                    String stat = this.checkPin(phone, pin);
                    if( stat.equalsIgnoreCase("error")){
                        // return error
                        String err = this.getValueByKey("error_occured", labels)[0];
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                      
                    }
                    
                    else if(stat.equalsIgnoreCase("success")){
                        user.setNwpin(3);
                        /*
                        Here the PIN is successful, 
                        I would list account available in the system attached to the client's number
                        */
                        /*
                        i would update the position and return the list of accounts
                        */



                        String text = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en")){
                            //Send the first menu if french
                            if(user.getPreval().equalsIgnoreCase("3")){
                                //text = "Select Account to debit";
                                text = this.getValueByKey("select_acc_debit", labels)[0];
                            }
                            else if(user.getPreval().equalsIgnoreCase("4")){
                                //text = "Select Account to credit";
                                text = this.getValueByKey("select_acc_credit", labels)[0];
                            }
                            else{
                                //text = "Select an Account";
                                text = this.getValueByKey("select_acc", labels)[0];
                            }
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){
                            //Send the first menu if English
                            if(user.getPreval().equalsIgnoreCase("3")){
                                //text = "Selectionnez le compte a debiter";
                                text = this.getValueByKey("select_acc_debit", labels)[1];
                            }
                            else if(user.getPreval().equalsIgnoreCase("4")){
                                //text = "Selectionnez le compte a crediter";
                                text = this.getValueByKey("select_acc_dredit", labels)[1];
                            }
                            else{
                                //text = "Selectionnez un compte";
                                text = this.getValueByKey("select_acc", labels)[1];
                            }
                        }
                        
                        /*
                        i would call the get account webservices
                        */
                        String status = this.getAttachedAccounts(phone);
                        if(status.equalsIgnoreCase("error") || status.equalsIgnoreCase("fail")){
                            // return error
                            String err = this.getValueByKey("error_occured", labels)[0];
                            SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                       
                        }
                        else{
                            String accounts = "";
                            try{
                                JSONArray acc = new JSONArray(status);
                                user.setAcc(status);
                                for(int i=0; i<acc.length(); i++){
                                    accounts = accounts+ "\n"+new Double(i+1).longValue() +" : "+acc.getJSONObject(i).getString("cpt");
                                    
                                    System.out.println(accounts);
                                }
                                
                                text = text + accounts;
                            }
                            catch(Exception ex){
                                // return an error occured
                                
                                String err = this.getValueByKey("error_occured", labels)[0];
                                SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                       
                            }
                            
//                            
//                            text = text
//                                + "\n 1 : 372XXXXXX01 XAF CHEQUE PART"
//                                + "\n 2 : 373XXXXXX01 XAF CHEQUE PART";
                            //Change default langauge
                            user.setNwpin(4);
                            user.setPos(user.getPos() + TemplatesUSSD.getBaseSeparateurChoice() + userchoice + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                            SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text);
                            userRepo.saveAndFlush(user);
                            
                        }

                        



                    }
                    else{
                        if(user.getNwpin()==0){
                            //Account blocked PIN trials done
                            String account_blocked = "";
                            if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                //account_blocked = "Votre compte a ete bloque.\n Rapprochez-vous de votre Agence CCC PLC pour debloquer votre compte";
                                account_blocked = this.getValueByKey("blocked_account", labels)[1];
                            }
                            else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                //account_blocked = "Your Account has been blocked.\n Approach your CCC PLC Branch to unblock your Account";
                                account_blocked = this.getValueByKey("blocked_account", labels)[0];
                            }
                            user.setStatus("2");
                            user.setPos(null);
                            userRepo.saveAndFlush(user);
                            SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, account_blocked);


                        }

                        //incorrect PIN, -1 trials remaining
                        String text_gen = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //text_gen = "Code PIN incorrect, veuillez reessayer.\n"+(user.getNwpin()-1)+ " essais restants";
                            text_gen = this.getValueByKey("wrongpin_reenter", labels)[1]+" "+(user.getNwpin()-1)+ " essais restants";
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //text_gen = "Incorrect PIN, Please Enter your PIN again\n"+(user.getNwpin()-1) +" Trials remaining";
                            text_gen = this.getValueByKey("wrongpin_reenter", labels)[0] +" "+(user.getNwpin()-1) +" Trials remaining";;
                        }
                        user.setNwpin(user.getNwpin()-1);

                       // user.setPreval(userchoice);
                        userRepo.saveAndFlush(user);
                        //we request the PIN and proceed tolist the accounts
                        SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);

                    }


                }
                else{
                    //Invalid Data type, we would ass user to re-enter correct character
                    if(user.getInval()==0){
                        String invalid = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //invalid = "Reponse invalide.\nMerci de nous faire confiance";
                            invalid = this.getValueByKey("invalid_reply", labels)[1];
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //invalid = "Invalid Response.\nThanks for trusting us";
                            invalid = this.getValueByKey("invalid_reply", labels)[0];

                        }

                        user.setPos(null);
                        userRepo.saveAndFlush(user);
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);

                    }

                    String text_gen = "";
                    if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                        //text_gen = "Format incorrect, veuillez entrer votre code PIN a 6 chiffres";
                        text_gen = this.getValueByKey("invalid_format_pin", labels)[1];

                    }
                    else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                        //text_gen = "Incorrect format, Please Enter your 6 digit number";
                        text_gen = this.getValueByKey("invalid_format_pin", labels)[0];

                    }
                    user.setInval(user.getInval()-1);

                    //user.setPreval(userchoice);
                    userRepo.saveAndFlush(user);
                    //we request the PIN and proceed tolist the accounts
                    SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);


                }

            }

            else if(screen_number ==  2){
                System.out.println("Yann see this screen number: "+screen_number);
                //second screen
                /****
                 * This is after the first page, that is *init
                 */

                System.out.println(" i am here oooh see me");
                final String userchoice = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
                System.out.println(" userchoice "+userchoice);
                if (UtilsUssdscpg.isInt(userchoice)) {


                    System.out.println(" i am here oooh see me 1");
                    /*
                    This here i would do a switch and redirect to different pages

                    */
                    // send second menu
                    /*
                    i would test if english or french
                    */

                    // switch statement with int data type
                    String services_initially_selected = String.valueOf(user.getPos().charAt(6));//position of product
                    String acc_selected = userchoice;
                    user.setLib2(userchoice);
                    String age = ""; String cpt = "";
                    System.out.println("see this yann services_initially_selected "+services_initially_selected);

                    try{
                        JSONArray acc = new JSONArray(user.getAcc());
                        if(Integer.parseInt(acc_selected)-1>= acc.length()){
                            // invalid option selected
                            //end it, else
                            SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MtnNotifications, phone, "Invalid reply");
                        }
                        else{
                            cpt = acc.getJSONObject(Integer.parseInt(acc_selected)-1).getString("cpt");
                            age = acc.getJSONObject(Integer.parseInt(acc_selected)-1).getString("age");
                            

                        switch (services_initially_selected) {
                            case "1":
                                //this is the get Balanced
                                /*
                                i have the phone number, account position, call the check balance and return
                                */
                                String text_gen = "";
                                
                                /*
                                i would call the getbalance services here
                                */
                                String status = this.getBalance(phone, age,cpt);
                                if(status.equalsIgnoreCase("error") || status.equalsIgnoreCase("fail")){
                                    // return error
                                    
                                    String err = this.getValueByKey("error_occured", labels)[0];
                                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                       
                                }
                                else{
                                    
                                    String balance = status;
                                    
                                    
                                    if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                        text_gen = this.getValueByKey("balance_msg", labels)[1] + " "+cpt+" est : "+balance+" XAF.\nMerci de nous faire confiance";

                                    }
                                    else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                        text_gen = this.getValueByKey("balance_msg", labels)[0] +" "+cpt+" is : "+balance+"  XAF.\nThanks for trusting us";

                                    }

                                    user.setPreval(userchoice);
                                    user.setPos(null);
                                    userRepo.saveAndFlush(user);
                                    //we request the PIN and proceed tolist the accounts
                                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, text_gen);

                                    
                                }


                                
                                break;
                            case "2":
                                //This is for ministatement
                                //this is the get Balanced
                                /*
                                i have the phone number, account position, call the check balance and return
                                */
                                String min_status = this.getMiniStatment(phone, age,cpt);
                                if(min_status.equalsIgnoreCase("error") || min_status.equalsIgnoreCase("fail")){
                                    // return error
                                    String err = this.getValueByKey("error_occured", labels)[0];
                                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                       
                                }
                                else{
                                    
                                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"); 
                                    
                                    SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd");
                                    

                                    String minist = "";
                                    try{
                                        JSONArray mini = new JSONArray(min_status);
                                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                        
                                            for(int i = 0; i<mini.length(); i++){
                                                Date date = dt.parse(mini.getJSONObject(i).get("DCO").toString()); 
                                                
                                                minist = minist +
                                                        "\n"+ new Double(i+1).longValue() +": "+dt1.format(date)
                                                        +" "+mini.getJSONObject(i).get("LIB").toString().substring(0, 8)+
                                                        " "+mini.getJSONObject(i).get("MON").toString()+
                                                        " "+mini.getJSONObject(i).get("SEN").toString();
                                            }

                                            

                                        }
                                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                            for(int i = 0; i<mini.length(); i++){
                                                Date date = dt.parse(mini.getJSONObject(i).get("DCO").toString()); 
                                                
                                                minist = minist +
                                                        "\n"+ new Double(i+1).longValue() +": "+dt1.format(date)
                                                        +" "+mini.getJSONObject(i).get("LIB").toString().substring(0, 8)+
                                                        " "+mini.getJSONObject(i).get("MON").toString()+
                                                        " "+mini.getJSONObject(i).get("SEN").toString();
                                            }

                                        }

                                        user.setPreval(userchoice);
                                        user.setPos(null);
                                        userRepo.saveAndFlush(user);
                                        //we request the PIN and proceed tolist the accounts
                                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, minist);

                                    

                                        
                                    }
                                    catch(Exception e){
                                        String err = this.getValueByKey("error_occured", labels)[0];
                                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                       
                                        
                                    }
                                    
                                }
                                
                                break;
                            case "3":
                                //this is bank to momo
                                /*
                                we would request the amount and perform the operation
                                */
                                String enter_amt_bank2momo = "";
                                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                    //enter_amt_bank2momo = "Saisissez le montant que vous souhaitez crediter sur votre compte momo.";
                                    enter_amt_bank2momo = this.getValueByKey("enter_amt_bkmomo", labels)[1];
                                }
                                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                    //enter_amt_bank2momo = "Enter the amount your wish to credit your momo account with.";
                                    enter_amt_bank2momo = this.getValueByKey("enter_amt_momobk", labels)[0];
                                }

                                user.setPreval(userchoice);
                                user.setPos(user.getPos() + TemplatesUSSD.getBaseSeparateurChoice() + userchoice + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                                userRepo.saveAndFlush(user);
                                //we request the PIN and proceed tolist the accounts
                                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, enter_amt_bank2momo);

                                break;
                            case "4":
                                //this is momo to bank
                                /*
                                we would request the amount and perform the operation
                                */
                                String enter_amt_momo2bank = "";
                                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                    //enter_amt_momo2bank = "Saisissez le montant que vous souhaitez crediter sur votre compte CCC PLC.";
                                    enter_amt_momo2bank = this.getValueByKey("enter_amt_momobk", labels)[1];
                                }
                                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                    //enter_amt_momo2bank = "Enter the amount your wish to credit your CCC PLC account with";
                                    enter_amt_momo2bank = this.getValueByKey("enter_amt_momobk", labels)[0];
                                }

                                user.setPreval(userchoice);
                                user.setLib2(userchoice);
                                user.setPos(user.getPos() + TemplatesUSSD.getBaseSeparateurChoice() + userchoice + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                                userRepo.saveAndFlush(user);
                                //we request the PIN and proceed tolist the accounts
                                SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, enter_amt_momo2bank);


                                break;
                            default:

                                String text_gen2 = "";
                                if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                    //text_gen2 = "Reponse invalide.\nMerci de nous faire confiance";
                                    text_gen2 = this.getValueByKey("invalid_reply", labels)[1];

                                }
                                else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                    //text_gen2 = "Invalid Response.\nThanks for trusting us";
                                    text_gen2 = this.getValueByKey("invalid_reply", labels)[0];

                                }

                                user.setPos(null);
                                userRepo.saveAndFlush(user);
                                SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, text_gen2);

                            }
                            
                        }
                    }
                    catch(Exception ex){
                        // return an error occured
                        String err = this.getValueByKey("error_occured", labels)[0];
                        SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                       
                    }
                    
                    
                    

                }

                else {
                    String err = this.getValueByKey("error_occured", labels)[0];
                    SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                       
                }
            }

            else if(screen_number ==  3){
                System.out.println("Yann see this screen number: "+screen_number);
                //thrid screen
                /****
                 * here the amount has been entered and the user would confirm the operation with his/her PIN
                 */

                System.out.println(" i am here oooh see me");
                final String userchoice = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
                System.out.println("amount for momo2bank or bank2momo: "+userchoice);
                
                String amount = userchoice;//amount can have a limit, we do the control
                        
                if(!UtilsUssdscpg.isInt(userchoice)){
                    SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MtnNotifications, phone, "Invalid reply");
                
                }
                else{
                    String services_initially_selected = String.valueOf(user.getPos().charAt(6));//position of product
                    String account_initially_selected = user.getLib2();

                    if((new Double(amount).longValue()>500000)){
                        //Maximum amount is 500k,
                        //please enter a correct amount

                        //Invalid Data type, we would ass user to re-enter correct character
                        if(user.getInval()==0){
                            String invalid = "";
                            if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                //invalid = "Montant invalide.\nMerci de nous faire confiance";
                                invalid = this.getValueByKey("invalid_amt", labels)[1];

                            }
                            else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                //invalid = "Invalid Amount.\nThanks for trusting us";
                                invalid = this.getValueByKey("invalid_amt", labels)[0];

                            }

                            user.setPos(null);
                            userRepo.saveAndFlush(user);
                            SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);

                        }

                        String text_gen = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //text_gen = "Le montant maximum est de 500000 xaf, veuillez saisir un montant inferieur a celui";
                            text_gen = this.getValueByKey("max_amt_error", labels)[1];
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //text_gen = "Maximum Amount is 500000 xaf, please enter an amount below that";
                            text_gen = this.getValueByKey("max_amt_error", labels)[0];

                        }
                        user.setInval(user.getInval()-1);

                        user.setPreval(userchoice);
                        userRepo.saveAndFlush(user);
                        //we request the PIN and proceed tolist the accounts
                        SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);


                    }

                    else{
                        String text = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //french
                            //text ="Veuillez entrer votre code PIN pour confirmer cette operation";
                            text = this.getValueByKey("enter_pin_confirm_trans", labels)[1];
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //english
                            //text = "Please Enter your PIN to confirm this operation";
                            text = this.getValueByKey("enter_pin_confirm_trans", labels)[0];
                        }

                        user.setPreval(amount);
                        user.setNwpin(4);
                        user.setPos(user.getPos() + TemplatesUSSD.getBaseSeparateurChoice() + userchoice + TemplatesUSSD.getBaseSeparateurChoice() + TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode());
                        SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text);
                        userRepo.saveAndFlush(user);


                    }

                    
                }
                
                
                


            }
            
            else if(screen_number ==  4){
                System.out.println("Yann see this screen number: "+screen_number);
                //thrid screen
                /****
                 * here the amount has been entered and we call the webservice to perform the operation
                 * inside a thread, and return a message to the user that he or should would recieved a confirmation
                 */

                System.out.println(" i am here oooh see me");
                final String userchoice = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
                String services_initially_selected = String.valueOf(user.getPos().charAt(6));//position of product
                String acc_selected = user.getLib2();
                String amount = user.getPreval();
                
                if (UtilsUssdscpg.isInt(userchoice)) {
                    
                    user.setInval(2);
                    String pin = userchoice;
                    /*
                    use the phone and PIN to check if its correct
                    using webservices
                    */
                            
                    String stat = this.checkPin(phone, pin);
                    if( stat.equalsIgnoreCase("error")){
                        // return error
                        String err = this.getValueByKey("error_occured", labels)[0];
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, /*"An Error occured, please try again later"*/err);
                      
                    }
                    
                    else if(stat.equalsIgnoreCase("success")){
                        /*
                        i would write a request payment thread and leave it here
                        */
                        JSONArray acc = new JSONArray(user.getAcc());
                        String cpt = acc.getJSONObject(Integer.parseInt(acc_selected)-1).getString("cpt");
                        String age = acc.getJSONObject(Integer.parseInt(acc_selected)-1).getString("age");
                        
                        
                        
                        
                        if(services_initially_selected.equalsIgnoreCase("3")){
                            
                            new Thread(() -> {
                                try {
                                    //i would call the request payment method here
                                    String status = this.requestPayment(phone, age, cpt, "bank_momo", amount, "Ussd B2W");
                                } catch (UnsupportedEncodingException ex) {
                                    Logger.getLogger(ManageUssdPosition.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }).start();
                            
                            String bank_momo = "";
                            if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                //bank_momo = "Nous avons initie votre operation, nous crediterons votre momo si l'operation reussit.\nThanks for trusting us";
                                bank_momo = this.getValueByKey("bankmomo_msg", labels)[1];
                            }
                            else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                //bank_momo = "We have initiated your operation, and would credit your momo once the operation is successful.\nThanks for trusting us";
                                bank_momo = this.getValueByKey("bankmomo_msg", labels)[0];
                            }

                            user.setPos(null);
                            userRepo.saveAndFlush(user);
                            SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, bank_momo);


                        }
                        else{
                            
                            new Thread(() -> {
                                try {
                                    //i would call the request payment method here
                                    String status = this.requestPayment(phone, age, cpt, "momo_bank", amount, "Ussd W2B");
                                } catch (UnsupportedEncodingException ex) {
                                    Logger.getLogger(ManageUssdPosition.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }).start();
                            
                            String momo_bank = "";
                            if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                //momo_bank = "Nous avons initie votre operation, merci de composer le *126# pour valider l'operation.\nMerci de nous faire confiance";
                                momo_bank = this.getValueByKey("momobank_msg", labels)[1];
                            }
                            else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                //momo_bank = "We have initiated your operation, please dail *126# to validate the operation.\nThanks for trusting us";
                                momo_bank = this.getValueByKey("momobank_msg", labels)[0];
                            }
                            user.setNwpin(3);
                            user.setPos(null);
                            userRepo.saveAndFlush(user);
                            SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, momo_bank);
                            

                        }
                        
                        
                    }
                    else{
                        if(user.getNwpin()==0){
                            //Account blocked PIN trials done
                            String account_blocked = "";
                            if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                                //account_blocked = "Votre compte a ete bloque.\n Rapprochez-vous de votre Agence CCC PLC pour debloquer votre compte";
                                account_blocked = this.getValueByKey("blocked_account", labels)[1];
                            }
                            else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                                //account_blocked = "Your Account has been blocked.\n Approach your CCC PLC Branch to unblock your Account";
                                account_blocked = this.getValueByKey("blocked_account", labels)[0];
                            }
                            user.setStatus("2");

                            user.setPos(null);
                            userRepo.saveAndFlush(user);
                            SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, account_blocked);


                        }

                        //incorrect PIN, -1 trials remaining
                        String text_gen = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //text_gen = "Code PIN incorrect, veuillez reessayer.\n"+(user.getNwpin()-1)+ " essais restants";
                            text_gen = this.getValueByKey("wrongpin_reenter", labels)[1] +" "+(user.getNwpin()-1)+ " essais restants";
                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //text_gen = "Incorrect PIN, Please Enter your PIN again\n"+(user.getNwpin()-1) +" Trials remaining";
                            text_gen = this.getValueByKey("wrongpin_reenter", labels)[0] +" "+(user.getNwpin()-1) +" Trials remaining";
                        }
                        user.setNwpin(user.getNwpin()-1);

                       // user.setPreval(userchoice);
                        userRepo.saveAndFlush(user);
                        //we request the PIN and proceed tolist the accounts
                        SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);

                    }


                }
                else{
                    //Invalid Data type, we would ass user to re-enter correct character
                    if(user.getInval()==0){
                        String invalid = "";
                        if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                            //invalid = "Reponse invalide.\nMerci de nous faire confiance";
                            invalid = this.getValueByKey("invalid_reply", labels)[1];

                        }
                        else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                            //invalid = "Invalid Response.\nThanks for trusting us";
                            invalid = this.getValueByKey("invalid_reply", labels)[0];

                        }

                        user.setPos(null);
                        userRepo.saveAndFlush(user);
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, invalid);

                    }

                    String text_gen = "";
                    if(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr")){
                        //text_gen = "Format incorrect, veuillez entrer votre code PIN a 6 chiffres";
                        text_gen = this.getValueByKey("invalid_format_pin", labels)[1];

                    }
                    else if(Objects.isNull(user.getLang()) ||(!Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("en"))){
                        //text_gen = "Incorrect format, Please Enter your 6 digit number";
                        text_gen = this.getValueByKey("invalid_format_pin", labels)[0];

                    }
                    user.setInval(user.getInval()-1);

                    //user.setPreval(userchoice);
                    userRepo.saveAndFlush(user);
                    //we request the PIN and proceed tolist the accounts
                    SendUssdUtils.generateContinueUssdCheckUserInput(MtnNotifications, phone, text_gen);


                }
                

            }
            /*    
            else if (user.getPos().contains(TemplatesUSSD.getBaseUserPositionOnStringUssdWaitingResponseCode()) && (user.getPos().contains(TemplatesUSSD.getBaseUserPositionOnStringUssd()))){
                final String[] splitDataStrings = user.getPos().split(TemplatesUSSD.getBaseSeparateurChoiceSplittingString());
                System.out.println("Somwhere "+splitDataStrings);

                if (splitDataStrings.length == TemplatesUSSD.getPositionselectedvehicle()) {
                    Integer userchoiceInteger;
                    try {
                        userchoiceInteger = Integer.valueOf(UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim());
                        System.out.println("yan see this "+userchoiceInteger);
                        user.setPos(null);
                        userRepo.saveAndFlush(user);
                        String text = "Thank you for trusting us";
                        SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, text);

                    }
                    catch (Exception ex) {
                        SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MtnNotifications, phone, "Invalid Respond");
                        return;
                    }

                }
                else {
                    final String userpassword = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
                    if (!UtilsUssdscpg.isInt(userpassword)) {
                        //if not interger reply with this
                        SendUssdUtils.generateErrorUserNotEnterINTEGERValue(MtnNotifications, phone, MessageUssdUtils.wrongValueSelect(phone, user.getLang(), messageSource));
                        return;
                    }
                    user.setPos(null);
                    userRepo.saveAndFlush(user);
                    String text = "Thank you for trusting us";
                    SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, text);
                }
            }*/
            else {
                final String input = UtilsUssd.getFullNameFromXml(MtnNotifications, TemplatesUSSD.getTagTexteString()).get(0).trim();
                //next condition
                System.out.println("yan see this am so lonley");
                user.setPos(null);
                userRepo.saveAndFlush(user);
                String text = "Thank you for trusting us";
                SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MtnNotifications, phone, text);
            }
        }
    }
    
    
    /*
    i would do services to check PIN , opt, get attached accounts, get account inval, get ministatement
    
    */
    
    public String checkPin(String tel, String pin) throws UnsupportedEncodingException{
        
        Map<String, String> request = new HashMap();
        request.put("tel", tel);
        request.put("pin", this.generateHash(pin));
        String url = digitalUrl+"/checkPinU";
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
                return "success";
            }
            else{
                return "fail";
            }
        }
        
        return "error";
    }
    
    public String getBalance(String tel, String age, String cpt){
        
        Map<String, String> request = new HashMap();
        request.put("tel", tel);
        request.put("age", age);
        request.put("cpt", cpt);
        String url = digitalUrl+"/getSoldeU";
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
                return res.get("current_balance").toString();
            }
            else{
                return "fail";
            }
        }
        
        return "error";
    }
    
    public String getMiniStatment(String tel, String age, String cpt){
        
        Map<String, String> request = new HashMap();
        request.put("tel", tel);
        request.put("age", age);
        request.put("cpt", cpt);
        String url = digitalUrl+"/getMinStatementU";
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
    
    public String getAttachedAccounts(String tel){
        Map<String, String> request = new HashMap();
        request.put("tel", tel);
        request.put("etab", "001");
        String url = digitalUrl+"/getCptRaU";
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
    
    public String checkOtpAccountAttachment(String tel, String otp, String age, String cpt) throws UnsupportedEncodingException{
        
        Map<String, String> request = new HashMap();
        request.put("tel", tel);
        request.put("cpt", cpt);
        request.put("otpCode", this.generateHash(otp));
        request.put("age", age);
        
        String url = digitalUrl+"/confirmCptAttachU";
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
                return "success";
            }
            else{
                return "fail";
            }
        }
        
        return "error";
    }
    
    public String changePIN(String tel, String oldpin, String newpin) throws UnsupportedEncodingException{
        
        Map<String, String> request = new HashMap();
        request.put("tel", tel);
        request.put("oldpin", this.generateHash(oldpin));
        request.put("newpin", this.generateHash(newpin));
        
        String url = digitalUrl+"/changePinU";
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
                return "success";
            }
            else{
                return "fail";
            }
        }
        
        return "error";
    }
    
    
    public String requestPayment(String tel, String age, String cpt, String type, String amount, String lib) throws UnsupportedEncodingException{
        
        Map<String, String> request = new HashMap();
        request.put("tel", tel);
        request.put("etab", "001");
        request.put("mtrans", amount);
        request.put("ccpt", "");
        request.put("typeco", "");
        request.put("dcpt", "");
        request.put("etab", "001");
        request.put("telop", tel);
        request.put("teldo", tel);
        request.put("lib", lib);
        request.put("nat", "");
        request.put("network", "1");
        request.put("top", "");
        
        if(type.equalsIgnoreCase("bank_momo")){
            request.put("top", "TSAM");
            request.put("typeco", "cpt_momo");
            request.put("nat", "cpt_momo");
            request.put("dcpt", age+cpt);
            
        }
        if(type.equalsIgnoreCase("momo_bank")){
            request.put("top", "TSMA");
            request.put("typeco", "momo_cpt");
            request.put("nat", "momo_cpt");
            request.put("ccpt", age+cpt);
        }
        
        
        
        String url = digitalUrl+"/requestPaiementU";
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
            if(res.get("success").toString().equalsIgnoreCase("1000")){
                return "pending";
            }
            else{
                return "fail";
            }
        }
        
        return "error";
    }
    
    
    /*
    request payments, functions
    */
    
    
    
    public String generateHash(String toHash) throws UnsupportedEncodingException {
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
}

