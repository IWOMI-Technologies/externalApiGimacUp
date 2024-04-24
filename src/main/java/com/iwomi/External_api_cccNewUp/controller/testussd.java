package com.iwomi.External_api_cccNewUp.controller;

import com.iwomi.External_api_cccNewUp.model.*;
import com.iwomi.External_api_cccNewUp.repository.*;
import com.iwomi.External_api_cccNewUp.ussd.service.UssdFirstTrustService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@RestController
@RequestMapping("${apiPrefix}")
@Component
@RequiredArgsConstructor

public class testussd {
    @Autowired
    UssdfirstpageRepository ussdfirstpageRepository;
    @Autowired
    UserSessionRepo usersRepo;
    @Autowired
    LabelRepository labelRepository;
    @Autowired
    HomepageRepository homepageRepository;
    @Autowired
    UssdFirstTrustService ussdFirstTrustService;
    @Autowired
    GimacpageRepository gimacpageRepository;
    @Autowired
    FtsltransRepository ftsltranspage ;
    @Autowired
    MyaccountRepository myaccountRepository;

    @RequestMapping(value = "/endpointgimac", method = RequestMethod.POST)
    ResponseEntity<String> enpointgimac(@RequestBody Map<String, Object> payload) {
        System.out.println("yvo login Test3 de USSD:  " + payload.toString());
        String msisdn1 = checkPayload(payload, "msisdn").toString();
        String sessionid1 = checkPayload(payload, "sessionid").toString();
        String message1 = checkPayload(payload, "message").toString();
        Map<String, Object> mess0 = checkPayload(payload, "message1");
        String message = payload.get("message").toString();
        String sessionid = payload.get("sessionid").toString();
        String msisdn = payload.get("msisdn").toString();
        String provider = payload.get("provider").toString();
        JSONObject map = new JSONObject();
        UserSession user = usersRepo.findClientByPhoneAndUuid(msisdn, sessionid);
        UserSession user2 = new UserSession();
        String text = "";

        List<labels> labels = labelRepository.findALL("0");
        List<Homepage> layout= homepageRepository.findActive()
                .stream()
                .sorted(Comparator.comparing(Homepage::getRang))
                .collect(Collectors.toList());
        /***
         * USSD START
         */
        if (user != null ) {
            /***
             * THIS IS THE GIMAC MENU PAGE
             */
            List<gimacpage> sortedMenu = gimacpageRepository.findActive()
                    .stream()
                    .sorted(Comparator.comparing(gimacpage::getRang))
                    .collect(Collectors.toList());

            /***
             * THIS IS THE FTSL MENU PAGE
             */
            List<Ussdfirstpage> sortedMenuftsl = ussdfirstpageRepository.findAllActive()
                    .stream()
                    .sorted(Comparator.comparing(Ussdfirstpage::getRang))
                    .collect(Collectors.toList());

            if (user.getLanguage() != null && user.getLanguage().equalsIgnoreCase("1")) {
                String pos = user.getPos();

                if (pos != null && pos.equalsIgnoreCase("1")) {
                    UserSession user3 = usersRepo.findClientByPhoneAndUuid(msisdn, sessionid);
                    user3.setMenulevel(message);
                    usersRepo.save(user3);
                }
                String ml = user.getMenulevel();
                
                /***
                 * THIS IS THE FTSL MENU LEVEL
                 */
                if (ml.equalsIgnoreCase("1")) {
                    
                    if (pos.equalsIgnoreCase("1")){
                        String menuElements = getValueByKey("menu_head", labels)[1];
                        for (Ussdfirstpage element : sortedMenuftsl){
                            menuElements += "\n" + element.getRang() + " : " + element.getValen();
                        }
                        text = menuElements;
                        map.put("message", text);
                        user.setPos("2");
                        user.setPreval("1");
                        user.setMenulevel("1");
                        usersRepo.save(user);
                        map.put("command", 1);
                        
                    } else if (pos.equalsIgnoreCase("2")) {
                        String sl = user.getSublevel();

                        /***
                         * MONEY TRANSFER
                         */
                        if (message.equalsIgnoreCase("1")){
                            List<Ftsltranspage> trans= ftsltranspage.findActive()
                                    .stream()
                                    .sorted(Comparator.comparing(Ftsltranspage::getRang))
                                    .collect(Collectors.toList());

                            String menuElements = getValueByKey("select_acc_credit", labels)[1];
                            for (Ftsltranspage element : trans){
                                menuElements += "\n" + element.getRang() + " : " + element.getValen();
                            }
                            text = menuElements;
                            map.put("message", text);
                            user.setPos("3");
                            user.setPreval(message);
                            user.setMenulevel("1");
                            user.setSublevel(message);
                            usersRepo.save(user);

                            /***
                             * CHECK ACCOUNT BALANCE
                             */
                        } else if (message.equalsIgnoreCase("2")) {
                            String menuElements = getValueByKey("enter_pin", labels)[1];
                            map.put("message", menuElements);
                            user.setPos("3");
                            user.setPreval(message);
                            user.setMenulevel("1");
                            user.setSublevel(message);
                            usersRepo.save(user);

                            /***
                             * MAKE LOANS
                             */
                        } else if (message.equalsIgnoreCase("3")) {

                            /***
                             * ACCOUNT MANAGEMENT
                             */
                        } else if (message.equalsIgnoreCase("4")) {
                            List<Myaccount> ls = myaccountRepository.findActive();
                            String menuElements = getValueByKey("acc_manag_page", labels)[1];
                            for (Myaccount element:ls){
                                menuElements += "\n" + element.getRang() + " : " + element.getValen();
                            }
                            map.put("message", menuElements);
                            user.setPos("3");
                            user.setPreval(message);
                            user.setMenulevel("1");
                            user.setSublevel(message);
                            usersRepo.save(user);
                        }else if (message.equalsIgnoreCase("7777")){

                        }else if (message.equalsIgnoreCase("9999")){

                        }else if (message.equalsIgnoreCase("99")){
                            String menuElements = getValueByKey("exit_msg", labels)[1];
                            map.put("message", menuElements);
                        }
                    } else if (pos.equalsIgnoreCase("3")) {

                    }

                    /***
                     * THIS IS THE GIMAC MENU LEVEL
                     */
                } else if (ml.equalsIgnoreCase("2")) {
                    if (pos.equalsIgnoreCase("1")){
                        String menuElements = getValueByKey("gimachomepage", labels)[1];
                        for (gimacpage element : sortedMenu){
                            menuElements += "\n" + element.getRang() + " : " + element.getValen();
                        }
                        text = menuElements;
                        map.put("message", text);
                        map.put("command", 1);
                    }
                }

            } else if (user.getLanguage() != null && user.getLanguage().equalsIgnoreCase("0")) {

            }

        }else {
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

                String menu_elements = this.getValueByKey("homepage", labels)[1];
                for (Homepage elements : layout) {
                    int va = elements.getRang();
                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                }
                text = menu_elements;
                map.put("message", text);
                map.put("command", 1);
        }

        return ResponseEntity.status(HttpStatus.OK).body(map.toString());
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
        return testRegularExpression1("\\d{6}$", PIN, max, iter);
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
            String result = "number of trials exceeded";
            res.put("result", result);
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
            String result = "number of trials exceeded";
            res.put("result", result);
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
            String result = "number of trials exceeded";
            res.put("result", result);
        }
        return res;

    }
    public Map<String, String> getsolde(@RequestBody Map<String, String> payload) {
        Map<String,Object>  obj =ussdFirstTrustService.getCpt1(payload) ;
        Map<String,Object>  obj1 =ussdFirstTrustService.getCli(payload) ;
        System.out.println(obj);
        return ussdFirstTrustService.getSolde(obj1.get("cli").toString(),obj.get("cpt").toString());
    }

    @RequestMapping(value = "/requestpayment1", method = RequestMethod.POST)
    public Map<String, String> requestPaiement(@RequestBody Map<String,String> payload) {
        Map<String, Object> obj = addlistfunction(payload);
        System.out.println(obj);
        return ussdFirstTrustService.addListPaiement(obj);
    }
    @RequestMapping(value = "/getwalletinq21", method = RequestMethod.POST)
    public Map<String, Object> getwalletinq2(@RequestBody Map<String, String> payload) {
        Map<String, Object> Obj = walfunc(payload);
        System.out.println();
        return ussdFirstTrustService.walletinq(Obj);
    }
    @RequestMapping(value = "/getwalletinquiry1", method = RequestMethod.POST)
    public Map<String, String> getwalletinqFTSL(@RequestBody Map<String, String> payload) {
        Map<String,Object>  obj =ussdFirstTrustService.getCpt1(payload) ;
        Map<String,Object>  obj1 =ussdFirstTrustService.getCli(payload) ;
        System.out.println(obj);
        return ussdFirstTrustService.walletinquiryftsl(obj1.get("cli").toString(),obj.get("cpt").toString());
    }
    @RequestMapping(value = "/billInquiry1", method = RequestMethod.POST)
    public Map<String, Object> billinquiry(@RequestBody Map<String, Object> payload) {
        Map<String, Object> Obj = billfunc(payload);
        System.out.println("fabrication object payment" + Obj);
        return ussdFirstTrustService.billdetails(Obj);
    }

    @RequestMapping(value = "/mobilereload1", method = RequestMethod.POST)
    public Map<String, Object> EtopUp(@RequestBody Map<String, Object> payload) {
        Map<String, Object> Obj = airtimefunc(payload);
        System.out.println("fabrication object payment" + Obj);
        return ussdFirstTrustService.airtimereload(Obj);
    }
    @RequestMapping(value = "/billpayment1", method = RequestMethod.POST)
    public Map<String, Object> paybill(@RequestBody Map<String, Object> payload) {
        //Map<String, Object> Obj = billinquiry(payload);
        return ussdFirstTrustService.billpayment(payload);
    }
    @RequestMapping(value = "/checkPinU1", method = RequestMethod.POST)
    public Map<String, Object> checkPinU(@RequestBody String tel,String pinuser) {
        String pin =hash(pinuser);
        return ussdFirstTrustService.CheckPin(tel,pin);
    }
    @RequestMapping(value = "/getNomencTabcdAcscd1", method = RequestMethod.POST)
    public Map<String, Object> getNomencTabcdAcscd(@RequestBody Map<String, String> payload) throws Exception {
        System.out.println("yvo recuperation en local: getNomencTabcd");
        System.out.println(payload);
        return ussdFirstTrustService.getNomencTabcdAcscd(payload.get("tabcd"),payload.get("acscd"),payload.get("etab"));
    }
    @RequestMapping(value = "/getCli1", method = RequestMethod.POST)
    public Map<String, Object> getcli(@RequestBody Map<String,String> telephone){
        return  ussdFirstTrustService.getCli(telephone);
    }
    @RequestMapping(value = "/getCpt1", method = RequestMethod.POST)
    public Map<String, Object> getcpt(@RequestBody Map<String,String> telephone){
        return  ussdFirstTrustService.getCpt1(telephone);
    }
    /*   @RequestMapping(value = "/getNomenDataByTabcd", method = RequestMethod.POST)
       public JSONObject getnomE (Map<String, String> payload) {
           return ussdFirstTrustService.makeOperation(payload);
       }*/
    public Map<String, Object> addlistfunction(Map<String, String> payload) {
        System.out.println("fabrication object payment" + payload);
        Map<String, Object> obj1 =ussdFirstTrustService.getCli(payload);
        Map<String, Object> response = new HashMap<>();
        response.put("etab", "001");
        response.put("type", "firstrust");
        response.put("region", payload.get("region"));
        response.put("nat", payload.get("nat"));
        response.put("cli", obj1.get("cli"));
        response.put("mtrans", payload.get("amount"));
        response.put("lib", "transfert wallet to wallet");
        response.put("typeco", "VIRE");
        response.put("network", "");
        response.put("top", "VIRE");
        response.put("tel", payload.get("telephone"));
        response.put("telop", "");
        response.put("pin", payload.get("pin"));
        response.put("codewaldo", "001");
        response.put("codewalop", payload.get("codewalop"));
        response.put("partnerid", "");
        response.put("partnerlib", "");
        response.put("member",payload.get("member"));
        response.put("transtype", "RE");
        response.put("recievercustmerdata", "");
        response.put("vouchercode", "");
        return response;
    }
    public Map<String, Object> billfunc (Map<String, Object> payload){
        Map<String,Object>response = new HashMap<>();
        response.put("intent", "bill_inquiry");
        response.put("member",payload.get("member"));
        response.put("codewaldo", payload.get("codewaldo"));
        response.put("serviceRef", "SRV_001");
        response.put("queryRef", "CTR_REF");
        response.put("contractRef", payload.get("contractRef"));
        System.out.println("fabrication de facturier" + payload.get("contractRef"));
        return response;
    }
    public Map<String, Object> airtimefunc (Map<String, Object> payload){
        Map<String,Object>response = new HashMap<>();
        response.put("intent", "mobile_reload");
        response.put("member",payload.get("member"));
        response.put("rmobile", payload.get("codewaldo"));
        response.put("smobile", payload.get("tel"));
        response.put("amount", payload.get("amount"));
        response.put("ref", payload.get("contractRef"));
        System.out.println("fabrication de facturier" + payload.get("contractRef"));
        return response;
    }
    public Map<String,Object> walfunc (Map<String,String> payload){
        Map<String,Object> resp = new HashMap<>();
        resp.put("intent", "account_inquiry");
        resp.put("member", payload.get("member"));
        resp.put("codewalop", payload.get("codewalop"));
        return resp;
    }

    public String hash (String pin){
        System.out.println("this is the pin: "+pin);
        return ussdFirstTrustService.generateHash(pin);
    }
    public Map<String, Object> checkPayload(Map<String, Object> payload, String key) {
        if (!payload.containsKey(key)) {
            payload.put(key, "");
        }
        return payload;
    }

}
