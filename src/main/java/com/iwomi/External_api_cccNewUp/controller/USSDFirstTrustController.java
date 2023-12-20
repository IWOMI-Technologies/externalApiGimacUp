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
    

      @RequestMapping(value = "/endpointold" , method =  RequestMethod.POST)
   // @Timed
   // @Transactional(timeout = 1200)
   ResponseEntity<String> enpointold(@RequestBody Map<String, Object> payload) {
        System.out.println("yvo login Test3 de USSD:  "+payload.toString());
        String msisdn=checkPayload(payload, "msisdn").toString();
        String sessionid= checkPayload(payload, "sessionid").toString();
        String message1=checkPayload(payload, "message").toString();
       Map<String, Object>  mess0=checkPayload(payload, "message1");
        String message =  payload.get("message").toString();
//       String mess =  payload.get("message1").toString();
//       String mess2 =  payload.get("message2").toString();
//       String mess3 =  payload.get("message3").toString();
//       String mess4 =  payload.get("message4").toString();

        String provider=checkPayload(payload, "provider").toString();
         JSONObject map = new JSONObject();
         String newLine = System.getProperty("line.separator");
         //String send="Welcome to First Trust" + newLine +" 2: Balance "+ newLine +" 3: Purchase "+ newLine +" 4: Statement "+ newLine +" 5: Enquiry "+ newLine +" 0: Exit";

       final UserSession user = usersRepo.findClientByPhone(msisdn).orElse(null);
       System.out.println(user);
       // would check the default language and know which message to display
       String text = "";
       String dele= "0";
       List<labels> labels = labelRepository.findALL("0");
       List<Ussdfirstpage> menu = ussdfirstpageRepository.findAllActive();
       List<Ussdfirstpage> sortedmenu = menu.stream().sorted(Comparator.comparing(Ussdfirstpage::getRang)).collect(Collectors.toList());
//       System.out.println("hello1");
//       System.out.println("message input "+mess);
//       System.out.println("message input "+mess1);
       if(message.equalsIgnoreCase("1")) {
           // Logic for checking balance goes here
           map.put("message", "Votres solde c'est : 5 000 000 FCFA");
           map.put("command", 1);
       } else if (message.equalsIgnoreCase("2")) {
           map.put("message","vos 5 dernieres transaction"+"\n"+"1. first transaction"+"\n"+"2. second transaction"+"\n"+"3. third transaction"+"\n"+"4. fourth transaction"+"\n"+"5. fifth transaction");
           map.put("command",1);

       }  else if (message.equalsIgnoreCase("3")) {
           map.put("message","enter a tel num");
           map.put("command",2);
          // String telephone =  payload.get("telephone").toString();
           if (message.equalsIgnoreCase("656834991")) {
               map.put("message","enter the amount");
               // String amount =  payload.get("amount")toString();
               if(message.equalsIgnoreCase("1")){
                   map.put("message","enter PIN");
                   if(message.equalsIgnoreCase("1111")){
                       map.put("message","transaction completed sucessfully");
                   }else {
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
               }else {
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
           map.put("message","enter a wallet num");
           map.put("command",2);
           if (message.equalsIgnoreCase("1")){
               map.put("message","enter pin");
               map.put("command",3);
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
       } else if (message.equalsIgnoreCase("5")) {
           map.put("message","enter old pin");
           map.put("command",2);
           if (message.equalsIgnoreCase("true")) {
               map.put("message","enter new pin");
           }else{
               String menu_elements = this.getValueByKey("changepin_success", labels)[1];
               for (Ussdfirstpage elements : sortedmenu) {
                   int va = elements.getRang();
                   menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
               }

               text = menu_elements;
               map.put("message", text);
               map.put("command", 3);
               // }
           }

       }else {
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
    @RequestMapping(value = "/endpoint" , method =  RequestMethod.POST)
    // @Timed
    // @Transactional(timeout = 1200)
    ResponseEntity<String> enpoint(@RequestBody Map<String, Object> payload) {
        System.out.println("yvo login Test3 de USSD:  "+payload.toString());
        String msisdn1=checkPayload(payload, "msisdn").toString();
        String sessionid1= checkPayload(payload, "sessionid").toString();
        String message1=checkPayload(payload, "message").toString();
        Map<String, Object>  mess0=checkPayload(payload, "message1");
        String message =  payload.get("message").toString();
        String sessionid =  payload.get("sessionid").toString();
        String msisdn =  payload.get("msisdn").toString();
        String provider =  payload.get("provider").toString();
        
        String provider1=checkPayload(payload, "provider").toString();
        JSONObject map = new JSONObject();
        String newLine = System.getProperty("line.separator");

         UserSession user = usersRepo.findClientByPhoneAndUuid(msisdn,sessionid);
        // would check the default language and know which message to display

        String text = "";
        String dele= "0";
        List<labels> labels = labelRepository.findALL("0");
        List<Ussdfirstpage> menu = ussdfirstpageRepository.findAllActive();
        List<Ussdfirstpage> sortedmenu = menu.stream().sorted(Comparator.comparing(Ussdfirstpage::getRang)).collect(Collectors.toList());
      
    if(user!=null){
        String pos = user.getPos();
        if(pos.equalsIgnoreCase("1")){
            UserSession user3 = usersRepo.findClientByPhoneAndUuid(msisdn,sessionid);
            user3.setMenulevel(message);
            usersRepo.save(user3);
        }
        String ml= user.getMenulevel();

          if(ml.equalsIgnoreCase("1")){
              System.out.println("this step is ok ml level");
              if(pos.equalsIgnoreCase("1")) {
                  System.out.println("this step is ok pos level1"+pos);
                // Making a transaction gos here
                map.put("message", "entrer le pay ou la zone ou vous voulez transactioner"+"\n"+"1. firsttrust to firsttrust"+"\n"+"2. other cameroon"+"\n" +"3. other CEMAC"+"\n"+"7777. precedent"+"\n"+"9999 . HOME"+"\n"+"0. Exit"+"\n");
                user.setPos("2");
                user.setPreval("1");
                user.setMenulevel("1");// keep it to menu message
                usersRepo.save(user);
                  System.out.println("this is the message value:"+message);
                map.put("command", 2);
            } else if (pos.equalsIgnoreCase("2")) {
                  System.out.println("this step is ok pos level2"+pos);
                  if(pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("1")){
                      System.out.println("this step is ok pos level1 s1"+pos);
                      map.put("message","please enter the phone or walletnumber");
                      user.setPos("3");
                      user.setPreval("2");
                      user.setTranstel(message);
                      user.setSublevel("1");
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("comand","3");
                  } else if (pos.equalsIgnoreCase("2") && message.equalsIgnoreCase("2")) {
                      System.out.println("hello this submenu2");
                      System.out.println("this step is ok pos level2 s2"+pos);
                      map.put("message","choose the bank to whom to want to transfer"+"\n"+"1. Bipay"+"\n"+"2. speedoh"+"\n" +"3. orange money"+"\n"+"4. mobile money"+"5. afriland"+"\n" +"6. cca"+"\n"+"\n"+"7777. precedent"+"\n"+"9999 . HOME"+"\n"+"0. Exit"+"\n");
                      user.setPos("3");
                      user.setSublevel("2");
                      user.setPreval("2");
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("comand","3");
                      
                  } else if (pos.equalsIgnoreCase("2")&&message.equalsIgnoreCase("3")) {
                      System.out.println("this step is ok pos level3 s4"+pos);
                      map.put("message","choose the contry for the transaction"+"\n"+"1. Gabon"+"\n"+"2. Tchad"+"\n" +"3. RCA"+"\n"+"4. Congo"+"\n" +"6. cca"+"\n"+"\n"+"7777. precedent"+"\n"+"9999 . HOME"+"\n"+"0. Exit"+"\n");
                      user.setPos("3");
                      user.setPreval("2");
                      user.setSublevel("3");
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("comand","3");

                  }else{
                      System.out.println("hello2");
                      String menu_elements = this.getValueByKey("menu_head", labels)[1];
                      for (Ussdfirstpage elements : sortedmenu) {
                          int va = elements.getRang();
                          menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                      }
                      text = menu_elements;

                      map.put("message", text);
                      map.put("command", 1);
                      UserSession user2 = new UserSession();
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
                  if (st.equalsIgnoreCase("1")){
                      map.put("message","enter the amount you want to send");
                      user.setPos("4");
                      user.setPreval("3");
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("comand","4");
                  } else if (st.equalsIgnoreCase("2")) {
                      if(message.equalsIgnoreCase("1")){
                          // you will add memeber accordingly to all the banks
                          map.put("message", "sending to bipay account enter the number ");
                          map.put("command", "4");
                      } else if (message.equalsIgnoreCase("2")) {
                          map.put("message", "sending to speedoh account enter the number ");
                          map.put("command", "4");
                      }else if (message.equalsIgnoreCase("3")) {
                          map.put("message", "sending to om account enter the number ");
                          map.put("command", "4");
                      }else if (message.equalsIgnoreCase("4")) {
                          map.put("message", "sending to momo account enter the number ");
                          map.put("command", "4");
                      }
                  } else if (st.equalsIgnoreCase("3")) {
                      if (message.equalsIgnoreCase("1")){
                          map.put("message","choose the bank to whom to want to transfer"+"\n"+"1. Bipay"+"\n"+"2. speedoh"+"\n" +"3. orange money"+"\n"+"4. mobile money"+"5. afriland"+"\n" +"6. cca"+"\n"+"\n"+"7777. precedent"+"\n"+"9999 . HOME"+"\n"+"0. Exit"+"\n");
                          user.setPos("4");
                          //user.setSublevel(message);
                          user.setPreval("3");
                          user.setMenulevel("1");// keep it to menu message
                          usersRepo.save(user);
                          map.put("comand","4");
                      } else if (message.equalsIgnoreCase("2")) {
                          map.put("message","choose the bank to whom to want to transfer"+"\n"+"1. Bipay"+"\n"+"2. speedoh"+"\n" +"3. orange money"+"\n"+"4. mobile money"+"5. afriland"+"\n" +"6. cca"+"\n"+"\n"+"7777. precedent"+"\n"+"9999 . HOME"+"\n"+"0. Exit"+"\n");
                          user.setPos("4");
                         // user.setSublevel(message);
                          user.setPreval("3");
                          user.setMenulevel("1");// keep it to menu message
                          usersRepo.save(user);
                          map.put("comand","4");
                      } else if (message.equalsIgnoreCase("3")) {
                          map.put("message","choose the bank to whom to want to transfer"+"\n"+"1. Bipay"+"\n"+"2. speedoh"+"\n" +"3. orange money"+"\n"+"4. mobile money"+"5. afriland"+"\n" +"6. cca"+"\n"+"\n"+"7777. precedent"+"\n"+"9999 . HOME"+"\n"+"0. Exit"+"\n");
                          user.setPos("4");
                         // user.setSublevel(message);
                          user.setPreval("3");
                          user.setMenulevel("1");// keep it to menu message
                          usersRepo.save(user);
                          map.put("comand","4");
                      }else if (message.equalsIgnoreCase("4")) {
                          map.put("message","choose the bank to whom to want to transfer"+"\n"+"1. Bipay"+"\n"+"2. speedoh"+"\n" +"3. orange money"+"\n"+"4. mobile money"+"5. afriland"+"\n" +"6. cca"+"\n"+"\n"+"7777. precedent"+"\n"+"9999 . HOME"+"\n"+"0. Exit"+"\n");
                          user.setPos("4");
                          //user.setSublevel(message);
                          user.setPreval("3");
                          user.setMenulevel("1");// keep it to menu message
                          usersRepo.save(user);
                          map.put("comand","4");
                      }else if (message.equalsIgnoreCase("5")) {
                          map.put("message","choose the bank to whom to want to transfer"+"\n"+"1. Bipay"+"\n"+"2. speedoh"+"\n" +"3. orange money"+"\n"+"4. mobile money"+"5. afriland"+"\n" +"6. cca"+"\n"+"\n"+"7777. precedent"+"\n"+"9999 . HOME"+"\n"+"0. Exit"+"\n");
                          user.setPos("4");
                          //user.setSublevel(message);
                          user.setPreval("3");
                          user.setMenulevel("1");// keep it to menu message
                          usersRepo.save(user);
                          map.put("comand","4");
                      }
                      System.out.println("pos 4 level");
                  }
              }else if (pos.equalsIgnoreCase("4")){
                  System.out.println("pos 4 level1");
                  if(pos.equalsIgnoreCase("4") && user.getSublevel().equalsIgnoreCase("1")){
                      map.put("message"," you want to send xxxxxx to XXXXX XXXXXXX enter your pin for confirmation");
                      user.setPos("5");
                      user.setPin(message);
                      user.setPreval("4");
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("command","5");
                  }else if (pos.equalsIgnoreCase("4")&& user.getSublevel().equalsIgnoreCase("2")) {
                      map.put("message","please enter the phone or walletnumber");
                      user.setPos("5");
                      user.setPreval("4");
                      user.setTranstel(message);
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("comand","5");

                  }else if (pos.equalsIgnoreCase("4")&& user.getSublevel().equalsIgnoreCase("3")){
                      map.put("message","choose the bank to whom to want to transfer"+"\n"+"1. Bipay"+"\n"+"2. speedoh"+"\n" +"3. orange money"+"\n"+"4. mobile money"+"5. afriland"+"\n" +"6. cca"+"\n"+"\n"+"7777. precedent"+"\n"+"9999 . HOME"+"\n"+"0. Exit"+"\n");
                      user.setPos("5");
                      user.setPreval("4");
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("command","5");

                  }
              }else if(pos.equalsIgnoreCase("5")){
                  if (pos.equalsIgnoreCase("5")&& user.getSublevel().equalsIgnoreCase("1")){
                      map.put("message","transaction successfull");
                      map.put("command","0");

                  }else if (pos.equalsIgnoreCase("5")&& user.getSublevel().equalsIgnoreCase("2")) {
                      map.put("message","please enter the amount for transfer");
                      user.setPos("6");
                      user.setAmount(message);
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("command","6");

                  }else if (pos.equalsIgnoreCase("5")&& user.getSublevel().equalsIgnoreCase("3")) {

                      if(message.equalsIgnoreCase("1")){
                          // you will add memeber accordingly to all the banks
                          map.put("message", "sending to bipay account enter the number ");
                          user.setPos("6");
                          user.setAmount(message);
                          user.setMenulevel("1");// keep it to menu message
                          usersRepo.save(user);
                          map.put("command", "6");
                      } else if (message.equalsIgnoreCase("2")) {
                          map.put("message", "sending to speedoh account enter the number ");
                          user.setPos("6");
                          user.setAmount(message);
                          user.setMenulevel("1");// keep it to menu message
                          usersRepo.save(user);
                          map.put("command", "6");
                      }else if (message.equalsIgnoreCase("3")) {
                          map.put("message", "sending to om account enter the number ");
                          user.setPos("6");
                          user.setAmount(message);
                          user.setMenulevel("1");// keep it to menu message
                          usersRepo.save(user);
                          map.put("command", "6");
                      }else if (message.equalsIgnoreCase("4")) {
                          map.put("message", "sending to momo account enter the number ");
                          user.setPos("6");
                          user.setAmount(message);
                          user.setMenulevel("1");// keep it to menu message
                          usersRepo.save(user);
                          map.put("command", "6");
                      }

                  }

              }else  if (pos.equalsIgnoreCase("6")){
                  if(pos.equalsIgnoreCase("6")&& user.getSublevel().equalsIgnoreCase("2")){
                      map.put("message"," you want to send xxxxxx to XXXXX XXXXXXX enter your pin for confirmation");
                      user.setPos("7");
                      user.setPin(message);
                      user.setPreval("6");
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("command","7");
                  } else if (pos.equalsIgnoreCase("6")&& user.getSublevel().equalsIgnoreCase("3")) {
                      map.put("message","please enter the phone or walletnumber");
                      user.setPos("7");
                      user.setPreval("6");
                      user.setTranstel(message);
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("comand","7");
                  }
              }else  if (pos.equalsIgnoreCase("7")){

                  if(pos.equalsIgnoreCase("7")&& user.getSublevel().equalsIgnoreCase("2")){
                      map.put("message","transaction successfull thanks for trust");
                      map.put("command","0");
                  } else if (pos.equalsIgnoreCase("7")&& user.getSublevel().equalsIgnoreCase("3")) {
                      map.put("message","please enter the amount for transfer");
                      user.setPos("8");
                      user.setPreval("7");
                      user.setAmount(message);
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("command","8");
                  }
              } else if(pos.equalsIgnoreCase("8")){
                  if(pos.equalsIgnoreCase("8")&& user.getSublevel().equalsIgnoreCase("3")){
                      map.put("message"," you want to send xxxxxx to XXXXX XXXXXXX enter your pin for confirmation");
                      user.setPos("9");
                      user.setPin(message);
                      user.setPreval("8");
                      user.setMenulevel("1");// keep it to menu message
                      usersRepo.save(user);
                      map.put("command","9");
                  }
              }else if(pos.equalsIgnoreCase("9")){
                  map.put("message","transaction successfull thanks for trust");
                  map.put("command","0");

              }
          }
    }else {
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
           UserSession user2 = new UserSession();
           user2.setMenulevel("0");
           user2.setUuid(sessionid);
           user2.setPhone(msisdn);
           user2.setProvider(provider);
           user2.setPos("1");
           usersRepo.save(user2);
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
