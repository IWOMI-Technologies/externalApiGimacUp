/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.controller;

import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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


    @RequestMapping(value = "/endpointold", method = RequestMethod.POST)
        // @Timed
        // @Transactional(timeout = 1200)
    ResponseEntity<String> enpointold(@RequestBody Map<String, Object> payload) {
        System.out.println("yvo login Test3 de USSD:  " + payload.toString());
        String msisdn = checkPayload(payload, "msisdn").toString();
        String sessionid = checkPayload(payload, "sessionid").toString();
        String message1 = checkPayload(payload, "message").toString();
        Map<String, Object> mess0 = checkPayload(payload, "message1");
        String message = payload.get("message").toString();
//       String mess =  payload.get("message1").toString();
//       String mess2 =  payload.get("message2").toString();
//       String mess3 =  payload.get("message3").toString();
//       String mess4 =  payload.get("message4").toString();

        String provider = checkPayload(payload, "provider").toString();
        JSONObject map = new JSONObject();
        String newLine = System.getProperty("line.separator");
        //String send="Welcome to First Trust" + newLine +" 2: Balance "+ newLine +" 3: Purchase "+ newLine +" 4: Statement "+ newLine +" 5: Enquiry "+ newLine +" 0: Exit";

        final UserSession user = usersRepo.findClientByPhone(msisdn).orElse(null);
        System.out.println(user);
        // would check the default language and know which message to display
        String text = "";
        String dele = "0";
        List<labels> labels = labelRepository.findALL("0");
        List<Ussdfirstpage> menu = ussdfirstpageRepository.findAllActive();
        List<Ussdfirstpage> sortedmenu = menu.stream().sorted(Comparator.comparing(Ussdfirstpage::getRang)).collect(Collectors.toList());
//       System.out.println("hello1");
//       System.out.println("message input "+mess);
//       System.out.println("message input "+mess1);
        if (message.equalsIgnoreCase("1")) {
            // Logic for checking balance goes here
            map.put("message", "Votres solde c'est : 5 000 000 FCFA");
            map.put("command", 1);
        } else if (message.equalsIgnoreCase("2")) {
            map.put("message", "vos 5 dernieres transaction" + "\n" + "1. first transaction" + "\n" + "2. second transaction" + "\n" + "3. third transaction" + "\n" + "4. fourth transaction" + "\n" + "5. fifth transaction");
            map.put("command", 1);

        } else if (message.equalsIgnoreCase("3")) {
            map.put("message", "enter a tel num");
            map.put("command", 2);
            // String telephone =  payload.get("telephone").toString();
            if (message.equalsIgnoreCase("656834991")) {
                map.put("message", "enter the amount");
                // String amount =  payload.get("amount")toString();
                if (message.equalsIgnoreCase("1")) {
                    map.put("message", "enter PIN");
                    if (message.equalsIgnoreCase("1111")) {
                        map.put("message", "transaction completed sucessfully");
                    } else {
                        // if(!Objects.isNull(user) && user.getDele().equalsIgnoreCase("0") && ( !Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){
                        //String menu_elements = this.getValueByKey("enter_pin_confirm_trans", labels)[2];
                        //for (Ussdfirstpage elements : sortedmenu) {
                        //int va = elements.getRang();
                        //menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                        // }
                        //   text = menu_elements;
                        map.put("message", text);
                        map.put("command", 2);
                    }
                } else {
                    // if(!Objects.isNull(user) && user.getDele().equalsIgnoreCase("0") && ( !Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){
                    String menu_elements = this.getValueByKey("enter_amt_bkmomo", labels)[2];
                    for (Ussdfirstpage elements : sortedmenu) {
                        int va = elements.getRang();
                        menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                    }
                    text = menu_elements;
                    map.put("message", text);
                    map.put("command", 2);
                    // }
                }

            } else {
                // if(!Objects.isNull(user) && user.getDele().equalsIgnoreCase("0") && ( !Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){
                String menu_elements = this.getValueByKey("bankmomo_msg", labels)[2];
                for (Ussdfirstpage elements : sortedmenu) {
                    int va = elements.getRang();
                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                }
                text = menu_elements;
                map.put("message", text);
                map.put("command", 2);
                // }
            }

        } else if (message.equalsIgnoreCase("4")) {
            map.put("message", "enter a wallet num");
            map.put("command", 2);
            if (message.equalsIgnoreCase("1")) {
                map.put("message", "enter pin");
                map.put("command", 3);
            } else {
                // if(!Objects.isNull(user) && user.getDele().equalsIgnoreCase("0") && ( !Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){

                String menu_elements = this.getValueByKey("momobank_msg", labels)[2];
                for (Ussdfirstpage elements : sortedmenu) {
                    int va = elements.getRang();
                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                }

                text = menu_elements;
                //map.put("message", text);
                // map.put("command", 2);
                // }
            }
//       } else if (message.equalsIgnoreCase("5")) {
//           map.put("message","enter old pin");
//           map.put("command",2);
//           if (message.equalsIgnoreCase("true")) {
//               map.put("message","enter new pin");
//           }else{
//               String menu_elements = this.getValueByKey("changepin_success", labels)[1];
//               for (Ussdfirstpage elements : sortedmenu) {
//                   int va = elements.getRang();
//                   menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
//               }
//
//               text = menu_elements;
//               map.put("message", text);
//               map.put("command", 3);
//               // }
//           }

        } else {
            // if(!Objects.isNull(user) && user.getDele().equalsIgnoreCase("0") && ( !Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){
            System.out.println("hello2");
            String menu_elements = this.getValueByKey("menu_head", labels)[1];
            for (Ussdfirstpage elements : sortedmenu) {
                int va = elements.getRang();
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

    //main endpoint service function
    @RequestMapping(value = "/endpoint", method = RequestMethod.POST)
    ResponseEntity<String> enpoint(@RequestBody Map<String, Object> payload) {
        System.out.println("yvo login Test3 de USSD:  " + payload.toString());
        String msisdn1 = checkPayload(payload, "msisdn").toString();
        String sessionid1 = checkPayload(payload, "sessionid").toString();
        String message1 = checkPayload(payload, "message").toString();
        Map<String, Object> mess0 = checkPayload(payload, "message1");
        String message = payload.get("message").toString();
        String sessionid = payload.get("sessionid").toString();
        String msisdn = payload.get("msisdn").toString();
        String provider = payload.get("provider").toString();

        String provider1 = checkPayload(payload, "provider").toString();
        JSONObject map = new JSONObject();
        String newLine = System.getProperty("line.separator");

        UserSession user = usersRepo.findClientByPhoneAndUuid(msisdn, sessionid);
        // would check the default language and know which message to display
        UserSession user2 = new UserSession();

        Map<String, String> checkpinU = ussdFirstTrustService.CheckPin(msisdn,message);



        String text = "";
        String dele = "0";
        List<labels> labels = labelRepository.findALL("0");
        List<Ussdfirstpage> menu = ussdfirstpageRepository.findAllActive();
        List<Ussdfirstpage> sortedmenu = menu.stream().sorted(Comparator.comparing(Ussdfirstpage::getRang)).collect(Collectors.toList());

        if (user != null ) {

            int max1 = user.getMax();
            int iter1 = user.getIterator();
            int iter2 = user.getIteratorAMT();
            int iter3 = user.getIteratorPIN();
            String num = user.getTranstel();
            String amt = user.getAmount();
            String phone = user2.getPhone();


            //booundaries start
            //french language translation
            if (user.getLanguage() != null && user.getLanguage().equalsIgnoreCase("1")) {
                String pos = user.getPos();

                if (pos != null && pos.equalsIgnoreCase("1")) {
                    UserSession user3 = usersRepo.findClientByPhoneAndUuid(msisdn, sessionid);
                    user3.setMenulevel(message);
                    usersRepo.save(user3);
                }
                String ml = user.getMenulevel();

                if (ml.equalsIgnoreCase("1")) {
                    // System.out.println("this step is ok ml level");
                    if (pos.equalsIgnoreCase("1")) {
                        // System.out.println("this step is ok pos level1" + pos);
                        // Making a transaction gos here
                        map.put("message", "entrer le pay ou la zone ou vous voulez transactioner" + "\n" + "1. firsttrust a firsttrust" + "\n" + "2. Autre cameroon" + "\n" + "3. Autre CEMAC" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("1");// keep it to menu message
                        usersRepo.save(user);
                        System.out.println("this is the message value:" + message);
                        map.put("command", 2);

                    } else if (pos.equalsIgnoreCase("2")) {
                        System.out.println("this step is ok pos level2" + pos);
                        if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("1")) {
                            System.out.println("this step is ok pos level1 s1" + pos);
                            map.put("message", "veuillez entrer le numéro de téléphone ou le numero de portefeuille" + "\n" + "7777. precedent" + "\n" + "9999 . menu principal" + "\n" + "0. quitter" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setSublevel("1");
                            user.setMenulevel("1");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 3);
                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("2")) {
                            System.out.println("hello this submenu2");
                            System.out.println("this step is ok pos level2 s2" + pos);
                            map.put("message", "choisissez  la banque ou vous voulez faire le transfert" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . menu principal" + "\n" + "0. quitter" + "\n");
                            user.setPos("3");
                            user.setSublevel("2");
                            user.setPreval("2");
                            user.setMenulevel("1");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 3);

                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("3")) {
                            System.out.println("this step is ok pos level3 s4" + pos);
                            map.put("message", "choose the country for the transaction" + "\n" + "1. Gabon" + "\n" + "2. Tchad" + "\n" + "3. RCA" + "\n" + "4. Congo" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setSublevel("3");
                            user.setMenulevel("1");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 3);

                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("9999")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("1");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);

                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("7777")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("1");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);

                        } else {
                            System.out.println("hello2");
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            map.put("command", 1);
                            user2.setMenulevel(message);
                            user2.setUuid(sessionid);
                            user2.setPhone(msisdn);
                            user2.setProvider(provider);
                            user2.setPos("1");
                            usersRepo.save(user2);
                            // }
                        }

                    } else if (pos.equalsIgnoreCase("3")) {
                        String st = user.getSublevel();
                        //number verification
                        if (st.equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "entrer le pay ou la zone ou vous voulez transactioner" + "\n" + "1. firsttrust to firsttrust" + "\n" + "2. other cameroon" + "\n" + "3. other CEMAC" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("2");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else {
                                //call the function
                                Map<String, Object> verif = numberTestCM(message, max1, iter1);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "enter the amount you want to send" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("4");
                                    user.setPreval("3");
                                    user.setTranstel(message);
                                    user.setMenulevel("1");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 4);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter1) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("3");
                                        user.setIterator(iter1 + 1);
                                        user.setPreval("2");
                                        user.setMenulevel("1");
                                        usersRepo.save(user);
                                        map.put("command", 3);
                                    }

                                }
                            }

                        } else if (st.equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                // you will add memeber accordingly to all the banks
                                map.put("message", "sending to bipay account enter the number " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setSublevel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "sending to speedoh account enter the number " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setSublevel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "sending to om account enter the number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setSublevel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "sending to momo account enter the number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setSublevel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "entrer le pay ou la zone ou vous voulez transactioner" + "\n" + "1. firsttrust to firsttrust" + "\n" + "2. other cameroon" + "\n" + "3. other CEMAC" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("2");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 3);
                            }

                        } else if (st.equalsIgnoreCase("3")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setSublevel(message);
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setSublevel(message);
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setSublevel("3");
                                user.setPreval("3");
                                user.setSublevel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setSublevel(message);
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("5")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setSublevel(message);
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);
                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "entrer le pay ou la zone ou vous voulez transactioner" + "\n" + "1. firsttrust to firsttrust" + "\n" + "2. other cameroon" + "\n" + "3. other CEMAC" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("2");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 3);
                            }
                            System.out.println("pos 4 level 33");
                        }

                    } else if (pos.equalsIgnoreCase("4")) {
                        System.out.println("pos 4 level1");
                        if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("1")) {
                            if ((message.equalsIgnoreCase("9999"))) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "please enter the phone or walletnumber" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else {
                                System.out.println("hello test pos 4 ");
                                Map<String, Object> verif = amounttest(message, max1, iter2);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    System.out.println("value of result :" + verif.get("result"));
                                    System.out.println("hello test pos 4  see steve1");
                                    System.out.println("hello test pos 4 steve2");
                                    map.put("message", " you want to send "+ amt+ " to "+ num+ " enter your pin for confirmation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("5");
                                    user.setAmount(message);
                                    user.setPreval("4");
                                    user.setMenulevel("1");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 5);

                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter2) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("4");
                                        user.setIteratorAMT(iter2 + 1);
                                        user.setPreval("3");
                                        user.setMenulevel("1");
                                        usersRepo.save(user);
                                        map.put("command", 4);
                                    }
                                }

                            }

                        } else if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("2")) {

                            if ((message.equalsIgnoreCase("9999"))) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else {
                                Map<String, Object> verif = numberTestCM(message, max1, iter1);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "please enter the amount to transfer" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("5");
                                    user.setPreval("4");
                                    user.setTranstel(message);
                                    user.setMenulevel("1");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 5);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter1) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("4");
                                        user.setIterator(iter1 + 1);
                                        user.setPreval("3");
                                        user.setMenulevel("1");
                                        usersRepo.save(user);
                                        map.put("command", 4);
                                    }
                                }
                            }

                        } else if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("3")) {

                            if (message.equalsIgnoreCase("1")) {
                                // you will add memeber accordingly to all the banks
                                map.put("message", "sending to bipay account enter the wallet number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "sending to speedoh account enter the wallet number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "sending to om account enter the wallet number " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "sending to momo account enter the wallet number " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the country for the transaction" + "\n" + "1. Gabon" + "\n" + "2. Tchad" + "\n" + "3. RCA" + "\n" + "4. Congo" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            }

                        }
                    } else if (pos.equalsIgnoreCase("5")) {
                        if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "enter the amount you want to send" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setTranstel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else {
                                Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                if (verif.get("result").toString().equalsIgnoreCase("ok") && checkpinU.get("status").equalsIgnoreCase("01")) {
                                    map.put("message", "transaction successfull");
                                    map.put("command", 6);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")){
                                    if (max1 == iter3) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("5");
                                        user.setIteratorPIN(iter3 + 1);
                                        user.setPreval("4");
                                        user.setMenulevel("1");
                                        usersRepo.save(user);
                                        map.put("command", 5);
                                    }
                                }

                            }

                        } else if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("2")) {

                            if ((message.equalsIgnoreCase("9999"))) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "please enter the phone or walletnumber" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setTranstel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                user.setMenulevel("1");
                                map.put("command", 5);

                            } else {
                                Map<String, Object> verif = amounttest(message, max1, iter2);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "you want to send xxxxxx to XXXXX XXXXXXX enter your pin for confirmation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("6");
                                    user.setPreval("5");
                                    user.setAmount(message);
                                    user.setMenulevel("1");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 6);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter2) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("4");
                                        user.setIteratorAMT(iter2 + 1);
                                        user.setPreval("3");
                                        user.setMenulevel("1");
                                        usersRepo.save(user);
                                        map.put("command", 4);
                                    }
                                }
                            }

                        } else if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("3")) {
                            if ((message.equalsIgnoreCase("9999"))) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else {
                                Map<String, Object> verif = numberTestOthers(message, max1, iter1);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "enter the amount you want to transfer " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("6");
                                    user.setTranstel(message);
                                    user.setMenulevel("1");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 6);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter1) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("4");
                                        user.setIterator(iter1 + 1);
                                        user.setPreval("3");
                                        user.setMenulevel("1");
                                        usersRepo.save(user);
                                        map.put("command", 4);
                                    }
                                }

                            }
                        }

                    } else if (pos.equalsIgnoreCase("6")) {
                        if (pos.equalsIgnoreCase("6") && user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "please enter the amount to transfer" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setTranstel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 6);

                            } else {
                                Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "transaction successfull");
                                    user.setPos("7");
                                    user.setPreval("6");
                                    user.setMenulevel("1");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 7);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter3) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("6");
                                        user.setIteratorPIN(iter3 + 1);
                                        user.setPreval("5");
                                        user.setMenulevel("1");
                                        usersRepo.save(user);
                                        map.put("command", 4);
                                    }
                                }

                            }

                        } else if (pos.equalsIgnoreCase("6") && user.getSublevel().equalsIgnoreCase("3")) {

                            if ((message.equalsIgnoreCase("9999"))) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "sending to bipay account enter the wallet number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 6);
                            } else {
                                Map<String, Object> verif = amounttest(message, max1, iter2);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "you want to send xxxxxx to XXXXX XXXXXXX enter your pin for confirmation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("7");
                                    user.setPreval("6");
                                    user.setAmount(message);
                                    user.setMenulevel("1");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 7);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter2) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("6");
                                        user.setIteratorAMT(iter2 + 1);
                                        user.setPreval("5");
                                        user.setMenulevel("1");
                                        usersRepo.save(user);
                                        map.put("command", 4);
                                    }
                                }

                            }
                        }

                    } else if (pos.equalsIgnoreCase("7")) {
                        if (pos.equalsIgnoreCase("7") && user.getSublevel().equalsIgnoreCase("3")) {
                            Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                            if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                map.put("message", "transaction successfull thanks for trust");
                                map.put("command", 8);
                            } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                if (max1 == iter3) {
                                    map.put("message", verif.get("result").toString());
                                    map.put("command", 0);
                                } else {
                                    String lastms = verif.get("textformat").toString();
                                    map.put("message", lastms);
                                    user.setPos("7");
                                    user.setIteratorPIN(iter3 + 1);
                                    user.setPreval("6");
                                    user.setMenulevel("1");
                                    usersRepo.save(user);
                                    map.put("command", 7);
                                }
                            }

                        }
                    }
                } else if (ml.equalsIgnoreCase("2")) {

                    if (pos.equalsIgnoreCase("1")) {
                        map.put("message", "please enter your pin for verification" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("2");// keep it to menu message
                        usersRepo.save(user);
                        map.put("command", 2);

                    } else if (pos.equalsIgnoreCase("2")) {
                        if ((message.equalsIgnoreCase("9999"))) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("2");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 1);

                        } else if (message.equalsIgnoreCase("7777")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("2");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 1);
                        } else {
                            Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                            if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                Map<String, String> solde =getwalletInquiry(phone);
                                System.out.println(solde.get("data"));
                                map.put("message",solde.get("data") + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setPreval("2");
                                user.setMenulevel("2");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                if (max1 == iter3) {
                                    map.put("message", verif.get("result").toString());
                                    map.put("command", 0);
                                } else {
                                    String lastms = verif.get("textformat").toString();
                                    map.put("message", lastms);
                                    user.setPos("2");
                                    user.setIteratorPIN(iter3 + 1);
                                    user.setPreval("1");
                                    user.setMenulevel("2");
                                    usersRepo.save(user);
                                    map.put("command", 2);
                                }
                            }
                        }


                    }

                } else if (ml.equalsIgnoreCase("3")) {
                    if (pos.equalsIgnoreCase("1")) {
                        // billpayment goes here
                        map.put("message", "choose the country or the zone " + "\n" + "1. Cameroon" + "\n" + "2. Cemac Zone" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("3");
                        usersRepo.save(user);
                        System.out.println("this is the message value:" + message);
                        map.put("command", 2);

                    } else if (pos.equalsIgnoreCase("2")) {
                        if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("1")) {
                            map.put("message", "which bill  do you want to pay " + "\n" + "1. Eneo" + "\n" + "2. Camwater" + "\n" + "3. Guce" + "\n" + "4. Canal +" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("3");
                            user.setSublevel("1");
                            usersRepo.save(user);
                            System.out.println("this is the message value:" + message);
                            map.put("command", 3);
                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("2")) {
                            map.put("message", "Choose the Country for the transaction" + "\n" + "1. Gabon " + "\n" + "2. Tchad" + "\n" + "3. RCA" + "\n" + "4. Congo +" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("3");
                            user.setSublevel("2");
                            usersRepo.save(user);
                            System.out.println("this is the message value:" + message);
                            map.put("command", 3);
                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("9999")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("3");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 3);

                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("7777")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("3");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);
                        }
                    } else if (pos.equalsIgnoreCase("3")) {
                        if (pos.equalsIgnoreCase("3") && user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "please enter your eneo bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "please enter your camwater bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "please enter your Guce bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the country or the zone " + "\n" + "1. Cameroon" + "\n" + "2. Cemac Zone" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("2");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 3);
                            }
                        } else if (pos.equalsIgnoreCase("3") && user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the country or the zone " + "\n" + "1. Cameroon" + "\n" + "2. Cemac Zone" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("2");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 3);
                            }
                        }

                    } else if (pos.equalsIgnoreCase("4")) {
                        if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("1")) {

                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);
                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "which bill  do you want to pay " + "\n" + "1. Eneo" + "\n" + "2. Camwater" + "\n" + "3. Guce" + "\n" + "4. Canal +" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else {
                                map.put("message", "bill number xxxxxxxx has an amount of xxxxxxx and penalities of Xxxxxx bill holder X enter your pin for validation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPreval("4");
                                user.setPos("5");
                                user.setBillnum(message);
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            }

                        } else if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "please enter your sagessa bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 5);

                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "please enter your Airtel bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 5);

                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "please enter your Moov bill number");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 5);

                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "Choose the Country for the transaction" + "\n" + "1. Gabon " + "\n" + "2. Tchad" + "\n" + "3. RCA" + "\n" + "4. Congo +" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            }
                        }
                    } else if (pos.equalsIgnoreCase("5")) {
                        if (user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "which bill  do you want to pay " + "\n" + "1. Eneo" + "\n" + "2. Camwater" + "\n" + "3. Guce" + "\n" + "4. Canal +" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);

                            } else {
                                Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "payment successful");
                                    user.setPos("6");
                                    user.setPreval("5");
                                    user.setMenulevel("3");
                                    user.setSublevel("1");
                                    usersRepo.save(user);
                                    map.put("command", 6);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter3) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("5");
                                        user.setIteratorPIN(iter3 + 1);
                                        user.setPreval("4");
                                        user.setMenulevel("3");
                                        usersRepo.save(user);
                                        map.put("command", 2);
                                    }
                                }

                            }
                        } else if (user.getSublevel().equalsIgnoreCase("2")) {

                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else {
                                map.put("message", "bill number xxxxxxxx has an amount of xxxxxxx and penalities of Xxxxxx bill holder X" + "\n" + "do you want to pay" + "\n" + "1. yes" + "\n" + "2. no and exit" + "\n");
                                user.setPos("6");
                                user.setPreval("5");
                                user.setBillnum(message);
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 6);
                            }

                        }

                    } else if (pos.equalsIgnoreCase("6")) {
                        if (user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 6);
                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);
                            } else {
                                Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "payment successful");
                                    user.setPos("7");
                                    user.setPreval("6");
                                    user.setMenulevel("3");
                                    user.setSublevel("1");
                                    usersRepo.save(user);
                                    map.put("command", 7);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter3) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("6");
                                        user.setIteratorPIN(iter3 + 1);
                                        user.setPreval("5");
                                        user.setMenulevel("3");
                                        usersRepo.save(user);
                                        map.put("command", 2);
                                    }
                                }

                            }
                        }
                    }


                } else if (ml.equalsIgnoreCase("5")) {
                    //loan
                    if (pos.equalsIgnoreCase("1")) {
                        map.put("message", "choose the type of loan you want " + "\n" + "1. Decouvert" + "\n" + "2. consomation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("5");
                        usersRepo.save(user);
                        map.put("command", 2);
                    } else if (pos.equalsIgnoreCase("2")) {
                        if (message.equalsIgnoreCase("1")) {
                            map.put("message", "enter the motif for you borrowing" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("5");
                            user.setSublevel("1");
                            usersRepo.save(user);
                            map.put("command", 3);
                        } else if (message.equalsIgnoreCase("2")) {
                            map.put("message", "enter the motif for you borrowing" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("5");
                            user.setSublevel("2");
                            usersRepo.save(user);
                            map.put("command", 3);
                        } else if (message.equalsIgnoreCase("9999")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("5");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);
                        } else if (message.equalsIgnoreCase("7777")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setSublevel("4");
                            user.setMenulevel("5");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);
                        }

                    } else if (pos.equalsIgnoreCase("3")) {
                        if (user.getSublevel().equalsIgnoreCase("1")) {

                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("5");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the type of loan you want " + "\n" + "1. Decouvert" + "\n" + "2. consomation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("2");
                                user.setMenulevel("5");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else {
                                map.put("message", "enter your pin for validation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("5");
                                usersRepo.save(user);
                                map.put("command", 4);

                            }

                        } else if (user.getSublevel().equalsIgnoreCase("2")) {

                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("5");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the type of loan you want " + "\n" + "1. Decouvert" + "\n" + "2. consomation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("2");
                                user.setMenulevel("5");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else {
                                map.put("message", "enter the duration for the loan in month" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("5");
                                usersRepo.save(user);
                                map.put("command", 3);
                            }

                        }

                    } else if (pos.equalsIgnoreCase("4")) {

                        if (user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "enter the motif for you borrowing" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setMenulevel("5");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else {
                                Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "your account has been credited of 10000FCFA thanks for your trust");
                                    user.setPos("5");
                                    user.setPreval("1");
                                    user.setMenulevel("5");
                                    usersRepo.save(user);
                                    map.put("command", 5);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter3) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("3");
                                        user.setIteratorPIN(iter3 + 1);
                                        user.setPreval("2");
                                        user.setMenulevel("3");
                                        usersRepo.save(user);
                                        map.put("command", 2);
                                    }
                                }

                            }

                        } else if (user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("5");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "enter the motif for you borrowing" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setMenulevel("5");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else {
                                map.put("message", "how much do you want to borrow:" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("3");
                                user.setMenulevel("5");
                                usersRepo.save(user);
                                map.put("command", 4);
                            }

                        }
                    } else if (pos.equalsIgnoreCase("5")) {
                        if (user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("5");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "enter the duration for the loan in month" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("5");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);

                            } else {
                                map.put("message", "come to the agnecy with all the required document for completion and validation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("6");
                                user.setAmount(message);
                                user.setPreval("5");
                                user.setMenulevel("5");
                                usersRepo.save(user);
                                map.put("command", 0);
                            }
                        }
                    }

                } else if (ml.equalsIgnoreCase("6")) {
                    if (pos.equalsIgnoreCase("1")) {
                        map.put("message", "welcome to account management " + "\n" + "1. My language " + "\n" + "2. My transaction" + "\n" + "3. change pin" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("6");
                        //user.setSublevel("2");
                        usersRepo.save(user);
                        map.put("command", 2);
                    } else if (pos.equalsIgnoreCase("2")) {
                        if (message.equalsIgnoreCase("1")) {
                            map.put("message", "change the language here" + "\n" + "1. French " + "\n" + "2. My English" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("6");
                            user.setSublevel("1");
                            usersRepo.save(user);
                            map.put("command", 0);

                        } else if (message.equalsIgnoreCase("2")) {
                            map.put("message", "your last transaction" + "\n" + "1. first transaction" + "\n" + "2. second transaction" + "\n" + "3. third transaction" + "\n" + "4. fourth transaction" + "\n" + "5. fifth transaction" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("6");
                            user.setSublevel("2");
                            usersRepo.save(user);
                            map.put("command", 1);
                        } else if (message.equalsIgnoreCase("3")) {
                            map.put("message", "enter your old pin");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("6");
                            user.setSublevel("3");
                            usersRepo.save(user);
                            map.put("command", 2);
                        } else if (message.equalsIgnoreCase("9999")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("6");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);

                        } else if (message.equalsIgnoreCase("7777")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setSublevel("4");
                            user.setMenulevel("6");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);
                        }

                    } else if (pos.equalsIgnoreCase("3")) {
                        if (user.getSublevel().equalsIgnoreCase("3")) {
                            Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                            if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                map.put("message", "enter the new pin");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("6");
                                user.setSublevel("3");
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                if (max1 == iter3) {
                                    map.put("message", verif.get("result").toString());
                                    map.put("command", 0);
                                } else {
                                    String lastms = verif.get("textformat").toString();
                                    map.put("message", lastms);
                                    user.setPos("3");
                                    user.setIteratorPIN(iter3 + 1);
                                    user.setPreval("2");
                                    user.setMenulevel("3");
                                    usersRepo.save(user);
                                    map.put("command", 2);
                                }
                            }


                        } else if (user.getSublevel().equalsIgnoreCase("1")) {
                            System.out.println("hello change language ");
                            if (message.equalsIgnoreCase("1")) {
                                System.out.println("hello change language ");
                                user.setLanguage("1");
                                user.setPos("1");
                                user.setPreval("0");
                                usersRepo.save(user);
                                map.put("command", 1);
                            } else if (message.equalsIgnoreCase("2")) {
                                System.out.println("hello change language ");
                                user.setLanguage("0");
                                user.setPos("1");
                                user.setPreval("0");
                                usersRepo.save(user);
                                map.put("command", 1);
                            }
                        }
                    } else if (pos.equalsIgnoreCase("4")) {
                        if (user.getSublevel().equalsIgnoreCase("3")) {
                            map.put("message", "confirm by entering again the new pin ");
                            user.setPos("5");
                            user.setPreval("4");
                            user.setMenulevel("6");
                            user.setSublevel("3");
                            usersRepo.save(user);
                            map.put("command", 3);
                        }
                    } else if (pos.equalsIgnoreCase("5")) {
                        if (user.getSublevel().equalsIgnoreCase("3")) {
                            map.put("message", "pin updated succesfully");
                            user.setPos("6");
                            user.setPreval("5");
                            user.setMenulevel("6");
                            user.setSublevel("3");
                            usersRepo.save(user);
                            map.put("command", 1);
                        }
                    }

                } else if (ml.equalsIgnoreCase("4")) {
                    if (pos.equalsIgnoreCase("1")) {
                        map.put("message", "entrer le pay ou la zone ou vous voulez transactioner" + "\n" + "1. CAMEROON " + "\n" + "2. other CEMAC country" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("4");// keep it to menu message
                        usersRepo.save(user);
                        System.out.println("this is the message value:" + message);
                        map.put("command", 2);

                    } else if (pos.equalsIgnoreCase("2")) {
                        if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("1")) {
                            map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. orange " + "\n" + "2. mtn" + "\n" + "3. Camtel" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setSublevel("1");
                            user.setMenulevel("4");
                            usersRepo.save(user);
                            map.put("command", 3);
                        } else if (pos.equalsIgnoreCase("2") && (message.equalsIgnoreCase("2"))) {
                            map.put("message", "choose the country for the transaction" + "\n" + "1. TCHAD " + "\n" + "2. GABON" + "\n" + "3. RCA" + "\n" + "4. CONGO" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("4");
                            user.setSublevel("2");
                            usersRepo.save(user);
                            map.put("command", 3);
                        } else if (pos.equalsIgnoreCase("2") && (message.equalsIgnoreCase("9999"))) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("4");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);

                        } else if (message.equalsIgnoreCase("7777")) {
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("4");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);
                        }

                    } else if (pos.equalsIgnoreCase("3")) {
                        String st = user.getSublevel();
                        if (st.equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "entrer le pay ou la zone ou vous voulez transactioner" + "\n" + "1. CAMEROON " + "\n" + "2. other CEMAC country" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("2");
                                user.setPreval("1");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 3);
                            }
                        } else if (st.equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. MOOV " + "\n" + "2. Airtel " + "\n" + "3. autres" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. MOOV " + "\n" + "2. Airtel " + "\n" + "3. Autres" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. MOOV " + "\n" + "2. Airtel " + "\n" + "3. Autres" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. MOOV " + "\n" + "2. Airtel " + "\n" + "3. Autres" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "entrer le pay ou la zone ou vous voulez transactioner" + "\n" + "1. CAMEROON " + "\n" + "2. other CEMAC country" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("2");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 3);
                            }
                        }
                    } else if (pos.equalsIgnoreCase("4")) {
                        if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. orange " + "\n" + "2. mtn" + "\n" + "3. Camtel" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setPreval("2");
                                user.setSublevel("1");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else {
                                Map<String, Object> verif = numberTestCM(message, max1, iter1);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "please enter the amount to transfer" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("5");
                                    user.setPreval("4");
                                    user.setTranstel(message);
                                    user.setMenulevel("4");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 5);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter1) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("4");
                                        user.setIterator(iter1 + 1);
                                        user.setPreval("3");
                                        user.setMenulevel("4");
                                        usersRepo.save(user);
                                        map.put("command", 2);
                                    }

                                }

                            }

                        } else if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setMenulevel("4");
                                user.setPreval("4");
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 5);

                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);
                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the country for the transaction" + "\n" + "1. TCHAD " + "\n" + "2. GABON" + "\n" + "3. RCA" + "\n" + "4. CONGO" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("3");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            }
                        }

                    } else if (pos.equalsIgnoreCase("5")) {
                        if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);
                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 5);

                            } else {
                                System.out.println("this is the message value:" + message);
                                System.out.println("this is the RYAN:" + pos);
                                Map<String, Object> verif = amounttest(message, max1, iter2);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    System.out.println("3" + verif.get("result"));
                                    map.put("message", "enter your pin" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("6");
                                    user.setPreval("5");
                                    user.setAmount(message);
                                    user.setMenulevel("4");
                                    usersRepo.save(user);
                                    map.put("command", 6);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter2) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("5");
                                        user.setIteratorAMT(iter2 + 1);
                                        user.setPreval("4");
                                        user.setMenulevel("4");
                                        usersRepo.save(user);
                                        map.put("command", 5);
                                    }
                                }

                            }

                        } else if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("2")) {

                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. MOOV " + "\n" + "2. Airtel " + "\n" + "3. autres" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else {
                                Map<String, Object> verif = numberTestOthers(message, max1, iter1);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "enter the amount to transfer" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("6");
                                    user.setPreval("5");
                                    user.setMenulevel("4");
                                    user.setTranstel(message);
                                    usersRepo.save(user);
                                    map.put("command", 6);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter1) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("5");
                                        user.setIterator(iter1 + 1);
                                        user.setPreval("4");
                                        user.setMenulevel("4");
                                        usersRepo.save(user);
                                        map.put("command", 5);
                                    }
                                }
                            }
                        }
                    } else if (pos.equalsIgnoreCase("6")) {
                        if (pos.equalsIgnoreCase("6") && user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "please enter the amount to transfer" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 6);
                            } else {
                                Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "transaction successfull" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("7");
                                    user.setPreval("6");
                                    user.setMenulevel("4");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 7);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter3) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("6");
                                        user.setIteratorPIN(iter3 + 1);
                                        user.setPreval("5");
                                        user.setMenulevel("4");
                                        usersRepo.save(user);
                                        map.put("command", 6);
                                    }
                                }
                            }
                        } else if (pos.equalsIgnoreCase("6") && user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage elements : sortedmenu) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;

                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("7777")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 6);

                            } else {
                                Map<String, Object> verif = amounttest(message, max1, iter2);
                                if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                    map.put("message", "enter your pin" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("7");
                                    user.setPreval("6");
                                    user.setAmount(message);
                                    user.setMenulevel("4");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 7);
                                } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                    if (max1 == iter2) {
                                        map.put("message", verif.get("result").toString());
                                        map.put("command", 0);
                                    } else {
                                        String lastms = verif.get("textformat").toString();
                                        map.put("message", lastms);
                                        user.setPos("6");
                                        user.setIteratorAMT(iter2 + 1);
                                        user.setPreval("5");
                                        user.setMenulevel("4");
                                        usersRepo.save(user);
                                        map.put("command", 6);
                                    }
                                }
                            }
                        }
                    } else if (pos.equalsIgnoreCase("7")) {
                        if (pos.equalsIgnoreCase("7") && user.getSublevel().equalsIgnoreCase("2")) {
                            Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                            if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                map.put("message", "transaction successfull thanks for trust");
                                user.setPos("8");
                                map.put("command", 8);
                            } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                if (max1 == iter3) {
                                    map.put("message", verif.get("result").toString());
                                    map.put("command", 0);
                                } else {
                                    String lastms = verif.get("textformat").toString();
                                    map.put("message", lastms);
                                    user.setPos("7");
                                    user.setIteratorPIN(iter3 + 1);
                                    user.setPreval("6");
                                    user.setMenulevel("4");
                                    usersRepo.save(user);
                                    map.put("command", 7);
                                }
                            }
                        }
                    }
                }


                // english language
            } else if (user.getLanguage() != null && user.getLanguage().equalsIgnoreCase("0")) {
                String pos = user.getPos();

                if (pos != null && pos.equalsIgnoreCase("1")) {
                    UserSession user3 = usersRepo.findClientByPhoneAndUuid(msisdn, sessionid);
                    user3.setMenulevel(message);
                    usersRepo.save(user3);
                }

//        else if (user.getLanguage().equalsIgnoreCase("0")) {
//            String pos = user.getPos();
//            if (pos.equalsIgnoreCase("1")) {
//                UserSession user3 = usersRepo.findClientByPhoneAndUuid(msisdn, sessionid);
//                user3.setMenulevel(message);
//                usersRepo.save(user3);
//            }
                String ml = user.getMenulevel();

                if (ml.equalsIgnoreCase("1")) {
                    System.out.println("this step is ok ml level");
                    if (pos.equalsIgnoreCase("1")) {
                        System.out.println("this step is ok pos level1" + pos);
                        // Making a transaction gos here
                        map.put("message", "entrer the country or yone to do transactions" + "\n" + "1. firsttrust to firsttrust" + "\n" + "2. other cameroon" + "\n" + "3. other CEMAC" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("1");// keep it to menu message
                        usersRepo.save(user);
                        System.out.println("this is the message value:" + message);
                        map.put("command", 2);
                    } else if (pos.equalsIgnoreCase("2")) {
                        System.out.println("this step is ok pos level2" + pos);
                        if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("1")) {
                            System.out.println("this step is ok pos level1 s1" + pos);
                            map.put("message", "please enter the phone or walletnumber" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
//                      user.setTranstel(message);
                            user.setSublevel("1");
                            user.setMenulevel("1");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 3);
                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("2")) {
                            System.out.println("hello this submenu2");
                            System.out.println("this step is ok pos level2 s2" + pos);
                            map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setSublevel("2");
                            user.setPreval("2");
                            user.setMenulevel("1");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 3);

                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("3")) {
                            System.out.println("this step is ok pos level3 s4" + pos);
                            map.put("message", "choose the country for the transaction" + "\n" + "1. Gabon" + "\n" + "2. Tchad" + "\n" + "3. RCA" + "\n" + "4. Congo" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setSublevel("3");
                            user.setMenulevel("1");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 3);

                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("9999")) {
                            map.put("command", 1);

                        } else {
                            System.out.println("hello2");
                            String menu_elements = this.getValueByKey("menu_head", labels)[1];
                            for (Ussdfirstpage elements : sortedmenu) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;

                            map.put("message", text);
                            map.put("command", 1);
                            user2.setMenulevel(message);
                            user2.setUuid(sessionid);
                            user2.setPhone(msisdn);
                            user2.setProvider(provider);
                            user2.setPos("1");
                            usersRepo.save(user2);
                            // }
                        }

                    } else if (pos.equalsIgnoreCase("3")) {
                        String st = user.getSublevel();
                        //number verification
                        if (st.equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "enter the amount you want to send" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            }

                        } else if (st.equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                // you will add memeber accordingly to all the banks
                                map.put("message", "sending to bipay account enter the number " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "sending to speedoh account enter the number " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "sending to om account enter the number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "sending to momo account enter the number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 0);
                            }
                        } else if (st.equalsIgnoreCase("3")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                //user.setSublevel(message);
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                // user.setSublevel(message);
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                // user.setSublevel(message);
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                //user.setSublevel(message);
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("5")) {
                                map.put("message", "choose the bank to whom to want to transfer" + "\n" + "1. Bipay" + "\n" + "2. speedoh" + "\n" + "3. orange money" + "\n" + "4. mobile money" + "5. afriland" + "\n" + "6. cca" + "\n" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                //user.setSublevel(message);
                                user.setPreval("3");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 0);
                            }
                            System.out.println("pos 4 level");
                        }
                    } else if (pos.equalsIgnoreCase("4")) {
                        System.out.println("pos 4 level1");
                        if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("1")) {
                            if ((message.equalsIgnoreCase("9999"))) {
                                map.put("command", 0);
                            } else {
                                map.put("message", " you want to send xxxxxx to XXXXX XXXXXXX enter your pin for confirmation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPin(message);
                                user.setPreval("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            }

                        } else if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("2")) {

                            if ((message.equalsIgnoreCase("9999"))) {
                                map.put("command", 0);
                            } else {
                                map.put("message", "please enter the amount to transfer" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setTranstel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            }


                        } else if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("3")) {

                            if (message.equalsIgnoreCase("1")) {
                                // you will add memeber accordingly to all the banks
                                map.put("message", "sending to bipay account enter the wallet number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 6);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "sending to speedoh account enter the wallet number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 6);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "sending to om account enter the wallet number " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 6);
                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "sending to momo account enter the wallet number " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 6);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 0);
                            }

                        }
                    } else if (pos.equalsIgnoreCase("5")) {
                        if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("1")) {
                            map.put("message", "transaction successfull");
                            map.put("command", 5);

                        } else if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("2")) {

                            if ((message.equalsIgnoreCase("9999"))) {
                                map.put("command", 0);
                            } else {
                                map.put("message", "you want to send xxxxxx to XXXXX XXXXXXX enter your pin for confirmation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("6");
                                user.setAmount(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 6);
                            }

                        } else if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("3")) {
                            if ((message.equalsIgnoreCase("9999"))) {
                                map.put("command", 0);
                            } else {
                                map.put("message", "enter the amount you want to transfer " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("6");
                                user.setAmount(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 6);
                            }
                        }

                    } else if (pos.equalsIgnoreCase("6")) {
                        if (pos.equalsIgnoreCase("6") && user.getSublevel().equalsIgnoreCase("2")) {
                            map.put("message", "transaction successfull");
                            user.setPos("7");
                            user.setPreval("6");
                            user.setMenulevel("1");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 6);
                        } else if (pos.equalsIgnoreCase("6") && user.getSublevel().equalsIgnoreCase("3")) {

                            if ((message.equalsIgnoreCase("9999"))) {
                                map.put("command", 0);
                            } else {
                                map.put("message", "you want to send xxxxxx to XXXXX XXXXXXX enter your pin for confirmation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("7");
                                user.setPreval("6");
                                user.setTranstel(message);
                                user.setMenulevel("1");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 7);
                            }
                        }
                    } else if (pos.equalsIgnoreCase("7")) {

                        if (pos.equalsIgnoreCase("7") && user.getSublevel().equalsIgnoreCase("3")) {
                            map.put("message", "transaction successfull thanks for trust");
                            map.put("command", 7);
                        }
                    }
                } else if (ml.equalsIgnoreCase("2")) {

                    if (pos.equalsIgnoreCase("1")) {

                    } else if (pos.equalsIgnoreCase("2")) {
                        if ((message.equalsIgnoreCase("9999"))) {
                            map.put("command", 0);
                        } else {
                            map.put("message", "you have 50000FCFA in your account" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("2");// keep it to menu message
                            usersRepo.save(user);
                            map.put("command", 2);
                        }


                    }

                } else if (ml.equalsIgnoreCase("3")) {
                    if (pos.equalsIgnoreCase("1")) {
                        // billpayment goes here
                        map.put("message", "choose the country or the zone " + "\n" + "1. Cameroon" + "\n" + "2. Cemac Zone" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("3");
                        usersRepo.save(user);
                        System.out.println("this is the message value:" + message);
                        map.put("command", 2);
                    } else if (pos.equalsIgnoreCase("2")) {
                        if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("1")) {
                            map.put("message", "which bill  do you want to pay " + "\n" + "1. Eneo" + "\n" + "2. Camwater" + "\n" + "3. Guce" + "\n" + "4. Canal +" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("3");
                            user.setSublevel("1");
                            usersRepo.save(user);
                            System.out.println("this is the message value:" + message);
                            map.put("command", 2);
                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("2")) {
                            map.put("message", "Choose the Country for the transaction" + "\n" + "1. Gabon " + "\n" + "2. Tchad" + "\n" + "3. RCA" + "\n" + "4. Congo +" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("3");
                            user.setSublevel("2");
                            usersRepo.save(user);
                            System.out.println("this is the message value:" + message);
                            map.put("command", 2);
                        } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("9999")) {
                            map.put("command", 1);
                        }
                    } else if (pos.equalsIgnoreCase("3")) {
                        if (pos.equalsIgnoreCase("3") && user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "please enter your eneo bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "please enter your camwater bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "please enter your Guce bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 3);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 0);
                            }
                        } else if (pos.equalsIgnoreCase("3") && user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "choose which bill you want to pay" + "\n" + "1. Sagessa" + "\n" + "2. AirtelGobon" + "\n" + "3. Moov" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 0);
                            }
                        }

                    } else if (pos.equalsIgnoreCase("4")) {
                        if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("1")) {

                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "bill number xxxxxxxx has an amount of xxxxxxx and penalities of Xxxxxx bill holder X" + "\n" + "do you want to pay" + "\n" + "1. yes" + "\n" + "2. no and exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("3");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            }

                        } else if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "please enter your sagessa bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "please enter your Airtel bill number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "please enter your Moov bill number");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 5);

                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            }
                        }
                    } else if (pos.equalsIgnoreCase("5")) {
                        if (user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "enter your pin for validation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("6");
                                user.setPreval("5");
                                user.setMenulevel("3");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 6);

                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "thanks for using first trust ");
                                user.setPos("6");
                                user.setPreval("5");
                                user.setMenulevel("3");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 0);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            }
                        } else if (user.getSublevel().equalsIgnoreCase("2")) {

                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("command", 0);
                            } else if (message.equalsIgnoreCase("1")) {
                                map.put("message", "bill number xxxxxxxx has an amount of xxxxxxx and penalities of Xxxxxx bill holder X" + "\n" + "do you want to pay" + "\n" + "1. yes" + "\n" + "2. no and exit" + "\n");
                                user.setPos("6");
                                user.setPreval("5");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 6);
                            } else {
                                map.put("message", "bill number xxxxxxxx has an amount of xxxxxxx and penalities of Xxxxxx bill holder X" + "\n" + "do you want to pay" + "\n" + "1. yes" + "\n" + "2. no and exit" + "\n");
                                user.setPos("6");
                                user.setPreval("5");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 6);
                            }

                        }

                    } else if (pos.equalsIgnoreCase("6")) {
                        if (user.getSublevel().equalsIgnoreCase("1")) {
                            map.put("message", "payment successful");
                            user.setPos("7");
                            user.setPreval("6");
                            user.setMenulevel("3");
                            user.setSublevel("1");
                            usersRepo.save(user);
                            map.put("command", 7);

                        } else if (user.getSublevel().equalsIgnoreCase("2")) {

                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "enter your pin for validation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("7");
                                user.setPreval("6");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 7);

                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "thanks for using first trust " + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("7");
                                user.setPreval("6");
                                user.setMenulevel("3");
                                user.setSublevel("2");
                                usersRepo.save(user);
                                map.put("command", 7);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else if (message.equalsIgnoreCase("0")) {
                                map.put("command", 0);
                            }
                        }
                    } else if (pos.equalsIgnoreCase("7")) {

                        if (user.getSublevel().equalsIgnoreCase("2")) {
                            map.put("message", "payment successful");
                            user.setPos("8");
                            user.setPreval("7");
                            user.setMenulevel("3");
                            user.setSublevel("2");
                            usersRepo.save(user);
                            map.put("command", 7);
                        }
                    }


                } else if (ml.equalsIgnoreCase("5")) {
                    //loan
                    if (pos.equalsIgnoreCase("1")) {
                        map.put("message", "choose the type of loan you want " + "\n" + "1. Decouvert" + "\n" + "2. consomation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("5");
                        //user.setSublevel("2");
                        usersRepo.save(user);
                        map.put("command", 2);
                    } else if (pos.equalsIgnoreCase("2")) {
                        if (message.equalsIgnoreCase("1")) {
                            map.put("message", "enter the motif for you borrowing" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("5");
                            user.setSublevel("1");
                            usersRepo.save(user);
                            map.put("command", 2);
                        } else if (message.equalsIgnoreCase("2")) {
                            map.put("message", "enter the motif for you borrowing" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("5");
                            user.setSublevel("2");
                            usersRepo.save(user);
                            map.put("command", 2);
                        } else if (message.equalsIgnoreCase("9999")) {
                            map.put("command", 1);
                        }

                    } else if (pos.equalsIgnoreCase("3")) {
                        if (user.getSublevel().equalsIgnoreCase("1")) {

                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "enter your pin for validation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("5");
                                usersRepo.save(user);
                                map.put("command", 3);
                            }

                        } else if (user.getSublevel().equalsIgnoreCase("2")) {

                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "enter the duration for the loan in month" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("5");
                                usersRepo.save(user);
                                map.put("command", 3);
                            }

                        }

                    } else if (pos.equalsIgnoreCase("4")) {

                        if (user.getSublevel().equalsIgnoreCase("1")) {
                            map.put("message", "your account has been credited of 10000FCFA thanks for your trust");
                            user.setPos("5");
                            user.setPreval("4");
                            user.setMenulevel("5");
                            usersRepo.save(user);
                            map.put("command", 5);
                        } else if (user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "how much do you want to borrow:" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("3");
                                user.setMenulevel("5");
                                usersRepo.save(user);
                                map.put("command", 4);
                            }

                        }
                    } else if (pos.equalsIgnoreCase("5")) {
                        if (user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "come to the agnecy with all the required document for completion and validation" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("6");
                                user.setPreval("5");
                                user.setMenulevel("5");
                                usersRepo.save(user);
                                map.put("command", 0);
                            }
                        }
                    }
                } else if (ml.equalsIgnoreCase("6")) {
                    if (pos.equalsIgnoreCase("1")) {
                        map.put("message", "walcome to account management " + "\n" + "1. My language " + "\n" + "2. My transaction" + "\n" + "3. change pin" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("6");
                        //user.setSublevel("2");
                        usersRepo.save(user);
                        map.put("command", 2);
                    } else if (pos.equalsIgnoreCase("2")) {
                        if (message.equalsIgnoreCase("1")) {
                            map.put("message", "change the language here" + "\n" + "1. French " + "\n" + "2. My English" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("6");
                            user.setSublevel("1");
                            usersRepo.save(user);
                            map.put("command", 0);
                        } else if (message.equalsIgnoreCase("2")) {
                            map.put("message", "your last transaction" + "\n" + "1. first transaction" + "\n" + "2. second transaction" + "\n" + "3. third transaction" + "\n" + "4. fourth transaction" + "\n" + "5. fifth transaction" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("6");
                            user.setSublevel("2");
                            usersRepo.save(user);
                            map.put("command", 1);
                        } else if (message.equalsIgnoreCase("3")) {
                            map.put("message", "enter your old pin");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("6");
                            user.setSublevel("3");
                            usersRepo.save(user);
                            map.put("command", 2);
                        } else if (message.equalsIgnoreCase("9999")) {
                            map.put("command", 1);
                        }

                    } else if (pos.equalsIgnoreCase("3")) {
                        if (user.getSublevel().equalsIgnoreCase("3")) {
                            map.put("message", "enter the new pin");
                            user.setPos("4");
                            user.setPreval("3");
                            user.setMenulevel("6");
                            user.setSublevel("3");
                            usersRepo.save(user);
                            map.put("command", 3);
                        } else if (user.getSublevel().equalsIgnoreCase("1")) {
                            System.out.println("hello change language ");
                            if (message.equalsIgnoreCase("1")) {
                                user.setPos("1");
                                user.setPreval("0");
                                user.setMenulevel("6");
                                user.setSublevel("1");
                                user.setLanguage("1");
                                usersRepo.save(user);
                                map.put("command", 1);
                            } else if (message.equalsIgnoreCase("2")) {
                                user.setLanguage("0");
                                user.setPos("1");
                                user.setPreval("0");
                                user.setMenulevel("6");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);

                            }
                        }
                    } else if (pos.equalsIgnoreCase("4")) {
                        if (user.getSublevel().equalsIgnoreCase("3")) {
                            map.put("message", "confirm by entering again the new pin ");
                            user.setPos("5");
                            user.setPreval("4");
                            user.setMenulevel("6");
                            user.setSublevel("3");
                            usersRepo.save(user);
                            map.put("command", 3);
                        }
                    } else if (pos.equalsIgnoreCase("5")) {
                        if (user.getSublevel().equalsIgnoreCase("3")) {
                            map.put("message", "pin updated succesfully");
                            user.setPos("4");
                            user.setPreval("3");
                            user.setMenulevel("6");
                            user.setSublevel("3");
                            usersRepo.save(user);
                            map.put("command", 1);
                        }
                    }
                } else if (ml.equalsIgnoreCase("4")) {
                    if (pos.equalsIgnoreCase("1")) {
                        map.put("message", "entrer le pay ou la zone ou vous voulez transactioner" + "\n" + "1. CAMEROON " + "\n" + "2. other CEMAC country" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("4");// keep it to menu message
                        usersRepo.save(user);
                        System.out.println("this is the message value:" + message);
                        map.put("command", 2);
                    } else if (pos.equalsIgnoreCase("2")) {
                        if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("1")) {
                            map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. orange " + "\n" + "2. mtn" + "\n" + "3. Camtel" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setSublevel("1");
                            user.setMenulevel("4");
                            usersRepo.save(user);
                            map.put("command", 3);
                        } else if (pos.equalsIgnoreCase("2") && (message.equalsIgnoreCase("2"))) {
                            map.put("message", "choose the country for the transaction" + "\n" + "1. TCHAD " + "\n" + "2. GABON" + "\n" + "3. RCA" + "\n" + "4. CONGO" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                            user.setPos("3");
                            user.setPreval("2");
                            user.setMenulevel("4");
                            user.setSublevel("2");
                            usersRepo.save(user);
                            map.put("command", 3);
                        } else if (pos.equalsIgnoreCase("2") && (message.equalsIgnoreCase("9999"))) {
                            map.put("command", 1);
                        }
                    } else if (pos.equalsIgnoreCase("3")) {
                        String st = user.getSublevel();
                        if (st.equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            }
                        } else if (st.equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. MOOV " + "\n" + "2. Airtel " + "\n" + "3. autres" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. MOOV " + "\n" + "2. Airtel " + "\n" + "3. Autres" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. MOOV " + "\n" + "2. Airtel " + "\n" + "3. Autres" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "choose the operator to whom you want to transfert" + "\n" + "1. MOOV " + "\n" + "2. Airtel " + "\n" + "3. Autres" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 4);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            }
                        }
                    } else if (pos.equalsIgnoreCase("4")) {
                        if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "please enter the amount to transfer" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setTranstel(message);
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 5);
                            }

                        } else if (pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("2")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setMenulevel("4");
                                user.setPreval("4");
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("3")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 5);

                            } else if (message.equalsIgnoreCase("4")) {
                                map.put("message", "enter a phone number" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("5");
                                user.setPreval("4");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 5);
                            } else if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            }
                        }
                    } else if (pos.equalsIgnoreCase("5")) {
                        if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "enter your pin" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("6");
                                user.setPreval("5");
                                user.setMenulevel("4");
                                usersRepo.save(user);
                                map.put("command", 6);
                            }

                        } else if (pos.equalsIgnoreCase("5") && user.getSublevel().equalsIgnoreCase("2")) {

                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "enter the amount to transfer" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("6");
                                user.setPreval("5");
                                user.setMenulevel("4");
                                user.setTranstel(message);
                                usersRepo.save(user);
                                map.put("command", 6);
                            }
                        }
                    } else if (pos.equalsIgnoreCase("6")) {
                        if (pos.equalsIgnoreCase("6") && user.getSublevel().equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "transaction successfull" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("7");
                                user.setPreval("6");
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 7);
                            }
                        } else if (pos.equalsIgnoreCase("6") && user.getSublevel().equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("9999")) {
                                map.put("command", 1);
                            } else {
                                map.put("message", "enter your pin" + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                user.setPos("7");
                                user.setPreval("6");
                                user.setTranstel(message);
                                user.setMenulevel("4");// keep it to menu message
                                usersRepo.save(user);
                                map.put("command", 7);
                            }

                        }
                    } else if (pos.equalsIgnoreCase("7")) {
                        if (pos.equalsIgnoreCase("7") && user.getSublevel().equalsIgnoreCase("2")) {
                            map.put("message", "transaction successfull thanks for trust");
                            user.setPos("8");
                            map.put("command", 8);
                        }
                    }
                }

            }
            //boundaries stop

        } else {
            // if(!Objects.isNull(user) && user.getDele().equalsIgnoreCase("0") && ( !Objects.isNull(user.getLang()) && user.getLang().equalsIgnoreCase("fr"))){
            System.out.println("hello2");
            user2.setMenulevel("0");
            user2.setUuid(sessionid);
            user2.setPhone(msisdn);
            user2.setMax(3);
            user2.setIteratorPIN(1);
            user2.setIteratorAMT(1);
            user2.setIterator(1);
            user2.setProvider(provider);
            user2.setLanguage("1");// for french language default
            user2.setPos("1");
            usersRepo.save(user2);

            UserSession user23 = usersRepo.findClientByPhoneAndUuid(msisdn, sessionid);
            if (user23.getLanguage().equalsIgnoreCase("1")) {
                String menu_elements = this.getValueByKey("menu_head", labels)[1];
                for (Ussdfirstpage elements : sortedmenu) {
                    int va = elements.getRang();

                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();

                    //menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                }
                text = menu_elements;

                map.put("message", text);
                map.put("command", 1);

            } else if (user23.getLanguage().equalsIgnoreCase("0")) {
                String menu_elements = this.getValueByKey("menu_head", labels)[0];
                for (Ussdfirstpage elements : sortedmenu) {
                    int va = elements.getRang();

                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();

                    //menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                }
                text = menu_elements;

                map.put("message", text);
                map.put("command", 1);

            }
            //String menu_elements = this.getValueByKey("menu_head", labels)[1];

            // }
        }

        return ResponseEntity.status(HttpStatus.OK).body(map.toString());
        //return map;
    }


    @RequestMapping(value = "/endpoint/{msisdn}/{sessionid}/{message}/{provider}", method = RequestMethod.GET)
    ResponseEntity<String> enpoint1(@PathVariable String msisdn, @PathVariable String sessionid, @PathVariable String message, @PathVariable String provider) {
        System.out.println("yvo login Test3 de USSD:  " + provider);
        //String msisdn=checkPayload(payload, "msisdn").toString();
        // String sessionid= checkPayload(payload, "sessionid").toString();
        // String message=checkPayload(payload, "message").toString();
        //String provider=checkPayload(payload, "provider").toString();
        JSONObject map = new JSONObject();
        String newLine = System.getProperty("line.separator");
        String send = "Welcome to First Trust" + newLine + " 2: Balance " + newLine + " 3: Purchase " + newLine + " 4: Statement " + newLine + " 5: Enquiry " + newLine + " 0: Exit";

        map.put("message", send);
        map.put("command", "1");
        return ResponseEntity.status(HttpStatus.OK).body(map.toString());
    }

    @RequestMapping(value = "/endpoint2/{provider}", method = RequestMethod.GET)
    public Map<String, String> enpoint2(@PathVariable String provider) {
        System.out.println("yvo login Test3 de USSD:  " + provider);
        //String msisdn=checkPayload(payload, "msisdn").toString();
        // String sessionid= checkPayload(payload, "sessionid").toString();
        // String message=checkPayload(payload, "message").toString();
        //String provider=checkPayload(payload, "provider").toString();
        Map<String, String> map = new HashMap();
        String newLine = System.getProperty("line.separator");
        String send = "Welcome to First Trust" + newLine + " 2: Balance " + newLine + " 3: Purchase " + newLine + " 4: Statement " + newLine + " 5: Enquiry " + newLine + " 0: Exit";
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

    public String getInvalAccounts(String tel) {
        Map<String, String> request = new HashMap();
        request.put("tel", tel);
        request.put("etab", "001");
        String url = digitalUrl + "/getCptInvAttachU";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(url, entity, String.class);
        } catch (HttpStatusCodeException ex) {
            System.out.println("yann see this ex " + ex.getMessage());
            System.out.println("Yankoo see this bro " + ResponseEntity.status(ex.getRawStatusCode()).headers(ex.getResponseHeaders()).body(ex.getResponseBodyAsString()));

        }
        JSONObject result = new JSONObject();
        System.out.println("yann see this res string " + response.toString());

        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            if (res.get("success").toString().equalsIgnoreCase("01")) {
                return res.get("data").toString();
            } else {
                return "fail";
            }
        }

        return "error";
    }

    public String[] getValueByKey(String key, List<labels> labels) {
        String[] val = new String[3];
        val[0] = null;
        val[1] = null;

        for (labels lable : labels) {
            if (lable.getKey().equalsIgnoreCase(key)) {
                val[0] = lable.getValen();
                val[1] = lable.getValfr();
            }
        }

        return val;

    }


    public Map<String, Object> numberTestCM(String number, int max, int iter) {
        // Regular expression for a maximum of 12 digits starting with "2376"
        return testRegularExpression("^2376\\d{8}$", number, max, iter);
    }

    public Map<String, Object> numberTestOthers(String number, int max, int iter) {
        return testRegularExpression2("^\\d{3}6\\d{8}$", number, max, iter);
    }

    public Map<String, Object> amounttest(String amount, int max, int iter) {
        // Verification of the amount to verify
        return testRegularExpressiontest("^\\d+(\\.\\d+)?$", amount, max, iter);
    }

    public Map<String, Object> pinverifiaction(String PIN, int max, int iter) {
        // verification of PIN
        return testRegularExpression1("\\d{4}$", PIN, max, iter);
    }

    //for checking PIN
    private Map<String, Object> testRegularExpression1(String regex, String testString, int maxTrial, int iter) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(testString);
        Map<String, Object> res = new HashMap<>();

        if (iter < maxTrial + 1) {
            if (matcher.matches()) {
                //System.out.println("String '" + testString + "\n");
                String result = "ok";
                res.put("result", result);
            } else {
                int ts = maxTrial - iter;
                String mess = " invalid PIN, try again," + ts + " attempts remaining.";
                String result = "ok1";
                res.put("result", result);
                res.put("textformat", mess);
            }

        } else {
            String vs = "number of trials exceeded";
            res.put("result", vs);
        }
        return res;

    }
    //private static Scanner scanner = new Scanner(System.in);

    //for checking numbers
    private Map<String, Object> testRegularExpression(String regex, String testString, int maxTrial, int iter) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(testString);
        Map<String, Object> res = new HashMap<>();
        if (iter < maxTrial + 1) {
            if (matcher.matches()) {
                //System.out.println("String '" + testString + "\n");
                String result = "ok";
                res.put("result", result);
            } else {
                int ts = maxTrial - iter;
                String mess = " invalid phone number,enter a number that starts with 237 ," + ts + " attempts remaining.";
                String result = "ok1";
                res.put("result", result);
                res.put("textformat", mess);
            }

        } else {
            res.put("result", "number of trials exceeded");
        }
        return res;

    }

    //for checking numbers other than cameroon
    private Map<String, Object> testRegularExpression2(String regex, String testString, int maxTrial, int iter) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(testString);
        Map<String, Object> res = new HashMap<>();

        if (iter < maxTrial + 1) {
            if (matcher.matches()) {
                //System.out.println("String '" + testString + "\n");
                String result = "ok";
                res.put("result", result);
            } else {
                int ts = maxTrial - iter;
                String mess = " invalid phone number, enter a number that starts with 12 digit phone number ," + ts + " attempts remaining.";
                String result = "ok1";
                res.put("result", result);
                res.put("textformat", mess);
            }

        } else {
            res.put("result", "number of trials exceeded");
        }
        return res;

    }

    private Map<String, Object> testRegularExpressiontest(String regex, String amount, int maxTrial, int iter) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(amount);
        Map<String, Object> res = new HashMap<>();

        if (iter < maxTrial + 1) {
            if (matcher.matches()) {
                //System.out.println("String '" + testString + "\n");
                String result = "ok";
                res.put("result", result);
            } else {
                int ts = maxTrial - iter;
                String mess = " invalid amount, enter only digits ," + ts + " attempts remaining.";
                String result = "ok1";
                res.put("result", result);
                res.put("textformat", mess);
            }

        } else {
            res.put("result", "number of trials exceeded");
        }
        return res;

    }


    public Map<String, String> getwalletInquiry(@RequestBody String tel) {
        Map<String,Object>  obj =ussdFirstTrustService.getcptByTel(tel);
        System.out.println(obj);
        return ussdFirstTrustService.getSolde(  obj.get("cli").toString(),  obj.get("cpt").toString());
    }
    @RequestMapping(value = "/getwalletInquiry", method = RequestMethod.POST)
    public Map<String, String> getwalletInquiry1(@RequestBody Map<String, String> payload) {
        Map<String,Object>  obj =ussdFirstTrustService.getcptByTel(payload.get("telephone")) ;
        Object ls = obj.get("data");

        System.out.println(obj);
        return ussdFirstTrustService.getSolde( obj.get("cli").toString(),  obj.get("cpt").toString());
    }

   /* @RequestMapping(value = "/byphone", method = RequestMethod.POST)
    public Map<String, Object> getwallet(@RequestBody Map<String, String> payload) {
        Map<String,Object>  obj =ussdFirstTrustService.getcptByTel(payload.get("telephone")) ;
        System.out.println(obj);
        return obj;
    }*/

    @RequestMapping(value = "/requestpayment", method = RequestMethod.POST)
    public Map<String, String> requestPaiement(@RequestBody Map<String, Object> payload) {
        return ussdFirstTrustService.addListPaiement("001", "0118004", "18004");
    }

    @RequestMapping(value = "/getwalletinq2", method = RequestMethod.POST)
    public Map<String, Object> getwalletinq2(@RequestBody Map<String, String> payload) {
        return ussdFirstTrustService.getcptByTel(payload.get("telephone"));
    }

    @RequestMapping(value = "/billInquiry", method = RequestMethod.POST)
    public Map<String, String> billinquiry(@RequestBody Map<String, Object> payload) {
        return ussdFirstTrustService.billdetails("001", "0118004", "18004");
    }

    @RequestMapping(value = "/checkPinU", method = RequestMethod.POST)
    public Map<String, String> checkPinU(@RequestBody Map<String, Object> payload) {
        return ussdFirstTrustService.CheckPin("694568752", "1234");
    }

    public Map<String, Object> checkPayload(Map<String, Object> payload, String key) {
        if (!payload.containsKey(key)) {
            payload.put(key, "");
        }
        return payload;
    }

}
