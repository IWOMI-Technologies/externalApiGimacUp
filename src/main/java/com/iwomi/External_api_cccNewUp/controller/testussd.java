package com.iwomi.External_api_cccNewUp.controller;

import com.iwomi.External_api_cccNewUp.model.*;
import com.iwomi.External_api_cccNewUp.repository.*;
import com.iwomi.External_api_cccNewUp.ussd.Dto.EditPinDto;
import com.iwomi.External_api_cccNewUp.ussd.Dto.TransactionDto;
import com.iwomi.External_api_cccNewUp.ussd.Enum.LibEnum;
import com.iwomi.External_api_cccNewUp.ussd.Enum.RegionEnum;
import com.iwomi.External_api_cccNewUp.ussd.service.UssdFirstTrustService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${apiPrefix}")
@Component
@RequiredArgsConstructor

public class testussd {

    @Value("${digitalbackoffice.url}")
    private String digitalUrl;
    private static final Map<String, Object> res = new HashMap<>();
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
    FtsltransRepository ftsltranspage;
    @Autowired
    MyaccountRepository myaccountRepository;
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    languageRepo languageRepo;
    @Autowired
    Back_homeRepo backHomeRepo;
    @Autowired
    CountryRepo countryRepo;
    @Autowired
    PartnerCmBkRepo partnerCmBkRepo;
    @Autowired
    PartnerCmWRepo partnerCmWRepo;
    @Autowired
    CmaccRepo cmaccRepo;

    @RequestMapping(value = "/endpoint", method = RequestMethod.POST)
    ResponseEntity<String> endpoint(@RequestBody Map<String, Object> payload) throws Exception {
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
        List<Homepage> layout = homepageRepository.findActive()
                .stream()
                .sorted(Comparator.comparing(Homepage::getRang))
                .collect(Collectors.toList());

        /***
         * USSD START
         */

        if (user != null) {
            int max1 = user.getMax();
            int iter1 = user.getIterator();
            int iter2 = user.getIteratorAMT();
            int iter3 = user.getIteratorPIN();
            String num = user.getTranstel();
            String amt = user.getAmount();
            String nat = user.getNat();
            String member = user.getMember();
            String region = user.getRegion();
            String billnum = user.getBillnum();
            String ref = user.getBillref();
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

            List<Back_home> backmenu = backHomeRepo.findActive()
                    .stream()
                    .sorted(Comparator.comparing(Back_home::getRang))
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

                    if (pos.equalsIgnoreCase("1")) {
                        String menuElements = getValueByKey("menu_head", labels)[1];
                        for (Ussdfirstpage element : sortedMenuftsl) {
                            menuElements += "\n" + element.getRang() + " : " + element.getValfr();
                        }
                        text = menuElements;
                        map.put("message", text);
                        user.setPos("2");
                        user.setPreval("1");
                        user.setNat("TRIN");
                        user.setMember("11010");//put the real member ftsl
                        user.setRegion("local");
                        user.setMenulevel("1");
                        usersRepo.save(user);
                        map.put("command", 2);

                    } else if (pos.equalsIgnoreCase("2")) {

                        /***
                         * MONEY TRANSFER
                         */
                        if (message.equalsIgnoreCase("1")) {
                            System.out.println("::::::::::::: transfert dargent :::::::::::");
                            System.out.println("::::::::::::: transfert dargent :::::::::::");
                            List<Ftsltranspage> trans = ftsltranspage.findActive()
                                    .stream()
                                    .sorted(Comparator.comparing(Ftsltranspage::getRang))
                                    .collect(Collectors.toList());

                            String menuElements = getValueByKey("select_acc_credit", labels)[1];
                            for (Ftsltranspage element : trans) {
                                menuElements += "\n" + element.getRang() + " : " + element.getValfr();
                            }
                            text = menuElements;
                            map.put("message", text);
                            user.setPos("3");
                            user.setPreval(message);
                            user.setMenulevel("1");
                            user.setSublevel(message);
                            usersRepo.save(user);
                            map.put("command", 3);

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
                            map.put("command", 3);

                            /***
                             * MAKE LOANS
                             */
                        } else if (message.equalsIgnoreCase("3")) {
                            List<Loans> DB = loanRepo.findActive();
                            System.out.println("rynnnnnn fabri" + DB);
                            String menuElements = getValueByKey("menu_head", labels)[1];
                            for (Loans element : DB) {
                                menuElements += "\n" + element.getRang() + " : " + element.getValfr();
                            }
                            map.put("message", menuElements);
                            user.setPos("3");
                            user.setPreval(message);
                            user.setMenulevel("1");
                            user.setSublevel(message);
                            usersRepo.save(user);
                            map.put("command", 3);

                            /***
                             * ACCOUNT MANAGEMENT
                             */
                        } else if (message.equalsIgnoreCase("4")) {
                            List<Myaccount> ls = myaccountRepository.findActive();
                            String menuElements = getValueByKey("acc_manag_page", labels)[1];
                            for (Myaccount element : ls) {
                                menuElements += "\n" + element.getRang() + " : " + element.getValfr();
                            }
                            map.put("message", menuElements);
                            user.setPos("3");
                            user.setPreval(message);
                            user.setMenulevel("1");
                            user.setSublevel(message);
                            usersRepo.save(user);
                            map.put("command", 3);

                        } else if (message.equalsIgnoreCase("7777")) {
                            String menu_elements = this.getValueByKey("homepage", labels)[1];
                            for (Homepage elements : layout) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;
                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("1");
                            usersRepo.save(user);
                            map.put("command", 1);

                        } else if (message.equalsIgnoreCase("9999")) {
                            String menu_elements = this.getValueByKey("homepage", labels)[1];
                            for (Homepage elements : layout) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;
                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("1");
                            usersRepo.save(user);
                            map.put("command", 1);

                        } else if (message.equalsIgnoreCase("99")) {
                            String menuElements = getValueByKey("exit_msg", labels)[1];
                            map.put("message", menuElements);
                            map.put("command", 0);
                        }

                    } else if (pos.equalsIgnoreCase("3")) {
                        String sl = user.getSublevel();
                        /***
                         * MONEY TRANSFER
                         */
                        if (sl.equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("1")) {
                                String menuElements = getValueByKey("WAL_credit", labels)[1];
                                map.put("message", menuElements);
                                user.setPos("4");
                                user.setPreval("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("2")) {
                                String menuElements = getValueByKey("ACC_credit", labels)[1];
                                map.put("message", menuElements);
                                user.setPos("4");
                                user.setPreval("2");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);

                            } else if (message.equalsIgnoreCase("7777")) {
                                String menuElements = getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage element : sortedMenuftsl) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValfr();
                                }
                                text = menuElements;
                                map.put("message", text);
                                user.setPos("2");
                                user.setPreval("1");
                                user.setNat("TRIN");
                                user.setMember("11010");//put the real member ftsl
                                user.setRegion("local");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);
                            }

                            /***
                             * CHECK ACCOUNT BALANCE
                             */
                        } else if (sl.equalsIgnoreCase("2")) {
                            Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                            if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                Map<String, Object> checkpinfinal = checkPinU(msisdn, message);
                                if (checkpinfinal.get("success").toString().equalsIgnoreCase("01")) {
                                    res.put("telephone", msisdn);
                                    Map<String, String> solde = getsolde(res);
                                    String menuElements = getValueByKey("balance_msg", labels)[1];
                                    map.put("message", solde.get("name") + menuElements + " : " + solde.get("solde") + "\n" + "7777. precedent" + "\n" + "9999 . HOME" + "\n" + "0. Exit" + "\n");
                                    user.setPos("4");
                                    user.setPreval("2");
                                    user.setMenulevel("1");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 4);
                                }
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
                                    user.setMenulevel("1");
                                    usersRepo.save(user);
                                    map.put("command", 3);
                                }
                            }
                            /***
                             * MAKE LOANS
                             */

                        } else if (sl.equalsIgnoreCase("3")) {
                            if (message.equalsIgnoreCase("1")) {
                                String menuElements = getValueByKey("loan_page", labels)[1];
                                map.put("message", menuElements);
                                user.setPos("4");
                                user.setPreval("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("2")) {
                                String menuElements = getValueByKey("loan_page", labels)[1];
                                map.put("message", menuElements);
                                user.setPos("4");
                                user.setPreval("2");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);

                            } else if (message.equalsIgnoreCase("7777")) {
                                String menuElements = getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage element : sortedMenuftsl) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                text = menuElements;
                                map.put("message", text);
                                user.setPos("2");
                                user.setPreval("1");
                                user.setNat("TRIN");
                                user.setMember("11010");//put the real member ftsl
                                user.setRegion("local");
                                user.setMenulevel("1");
                                usersRepo.saveAndFlush(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            }
                            /***
                             * ACCOUNT MANAGEMENT
                             */
                        } else if (sl.equalsIgnoreCase("4")) {
                            /***
                             * THIS IS FOR LANGUAGE
                             */
                            if (message.equalsIgnoreCase("1")) {
                                List<Language> lang = languageRepo.findActive();
                                String menuElements = getValueByKey("menu_head", labels)[1];
                                for (Language element : lang) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                map.put("message", menuElements);
                                user.setPos("4");
                                user.setMenulevel("1");
                                user.setPreval("1");
                                usersRepo.saveAndFlush(user);
                                map.put("command", 4);
                                /***
                                 * THIS IS TO VIEW YOUR TRANSACTIONS
                                 */
                            } else if (message.equalsIgnoreCase("2")) {
                                String menuElements = getValueByKey("enter_pin", labels)[1];
                                map.put("message", menuElements);
                                user.setPos("4");
                                user.setMenulevel("1");
                                user.setPreval("2");
                                usersRepo.saveAndFlush(user);
                                map.put("command", 4);
                                /***
                                 * THIS IS TO CHANGE PIN
                                 */
                            } else if (message.equalsIgnoreCase("3")) {
                                String menuElements = getValueByKey("enter_oldpin", labels)[1];
                                map.put("message", menuElements);
                                user.setPos("4");
                                user.setMenulevel("1");
                                user.setPreval("3");
                                usersRepo.saveAndFlush(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("7777")) {
                                String menuElements = getValueByKey("menu_head", labels)[1];
                                for (Ussdfirstpage element : sortedMenuftsl) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                text = menuElements;
                                map.put("message", text);
                                user.setPos("2");
                                user.setPreval("1");
                                user.setNat("TRIN");
                                user.setMember("11010");//put the real member ftsl
                                user.setRegion("local");
                                user.setMenulevel("1");
                                usersRepo.saveAndFlush(user);
                                map.put("command", 2);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            } else if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);
                            }
                        }

                    } else if (pos.equalsIgnoreCase("4")) {
                        String sl = user.getSublevel();
                        String pv = user.getPreval();
                        if (sl.equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);


                            } else if (message.equalsIgnoreCase("7777")) {
                                List<Ftsltranspage> trans = ftsltranspage.findActive()
                                        .stream()
                                        .sorted(Comparator.comparing(Ftsltranspage::getRang))
                                        .collect(Collectors.toList());

                                String menuElements = getValueByKey("select_acc_credit", labels)[1];
                                for (Ftsltranspage element : trans) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                text = menuElements;
                                map.put("message", text);
                                user.setPos("3");
                                user.setPreval("1");
                                user.setMenulevel("1");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 3);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            } else {
                                if (pv.equalsIgnoreCase("1")) {
                                    Map<String, Object> verif = numberTestCM(message, max1, iter1);
                                    if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                        String menuElements = getValueByKey("amount_msg", labels)[1];
                                        map.put("message", menuElements);
                                        user.setPos("5");
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
                                            user.setPreval("1");
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 4);
                                        }

                                    }

                                } else if (pv.equalsIgnoreCase("2")) {
                                    String menuElements = getValueByKey("amount_msg", labels)[1];
                                    map.put("message", menuElements);
                                    user.setPos("5");
                                    user.setTransacc(message);
                                    user.setMenulevel("1");// keep it to menu message
                                    usersRepo.save(user);
                                    map.put("command", 5);
                                }
                            }

                        } else if (sl.equalsIgnoreCase("2")) {
                            String menuElements = getValueByKey("exit_msg", labels)[1];
                            map.put("message", menuElements);
                            map.put("command", 0);

                        } else if (sl.equalsIgnoreCase("3")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);

                            } else if (message.equalsIgnoreCase("7777")) {
                                List<Loans> DB = loanRepo.findActive();
                                System.out.println("rynnnnnn fabri" + DB);
                                String menuElements = getValueByKey("menu_head", labels)[1];
                                for (Loans element : DB) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                map.put("message", menuElements);
                                user.setPos("3");
                                user.setPreval("3");
                                user.setMenulevel("1");
                                user.setSublevel("3");
                                usersRepo.save(user);
                                map.put("command", 3);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            } else {
                                if (pv.equalsIgnoreCase("1")) {
                                    Map<String, Object> verif = amounttest(message, max1, iter2);
                                    if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                        String menuElements = getValueByKey("borr_msg", labels)[1];
                                        map.put("message", menuElements);
                                        user.setPos("5");
                                        user.setAmount(message);
                                        user.setMenulevel("1");
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
                                            user.setIterator(iter2 + 1);
                                            user.setPreval("1");
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 3);
                                        }
                                    }

                                } else if (pv.equalsIgnoreCase("2")) {
                                    Map<String, Object> verif = amounttest(message, max1, iter2);
                                    if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                        String menuElements = getValueByKey("borr_msg", labels)[1];
                                        map.put("message", menuElements);
                                        user.setPos("5");
                                        user.setAmount(message);
                                        user.setMenulevel("1");
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
                                            user.setIterator(iter2 + 1);
                                            user.setPreval("1");
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 4);
                                        }
                                    }
                                }
                            }

                        } else if (sl.equalsIgnoreCase("4")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);

                            } else if (message.equalsIgnoreCase("7777")) {
                                List<Myaccount> ls = myaccountRepository.findActive();
                                String menuElements = getValueByKey("acc_manag_page", labels)[1];
                                for (Myaccount element : ls) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                map.put("message", menuElements);
                                user.setPos("3");
                                user.setPreval("4");
                                user.setMenulevel("1");
                                user.setSublevel("4");
                                usersRepo.save(user);
                                map.put("command", 3);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            } else {
                                if (pv.equalsIgnoreCase("1")) {
                                    /***
                                     * for changing language
                                     */

                                } else if (pv.equalsIgnoreCase("2")) {
                                    Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                    if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                        Map<String, Object> checkpinfinal = checkPinU(msisdn, message);
                                        if (checkpinfinal.get("success").toString().equalsIgnoreCase("01")) {
                                            /****
                                             * ADD TRANS API INFO OF THE USER
                                             * */
                                            String menuElements = getValueByKey("last_trans_msg", labels)[1];
                                            map.put("message", menuElements);
                                            user.setPos("5");
                                            user.setMenulevel("1");// keep it to menu message
                                            usersRepo.save(user);
                                            map.put("command", 5);
                                        }

                                    } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                        if (max1 == iter3) {
                                            map.put("message", "max trial exceeded please try again later");
                                            map.put("command", 0);
                                        } else {
                                            String lastms = verif.get("textformat").toString();
                                            map.put("message", lastms);
                                            user.setPos("4");
                                            user.setIteratorPIN(iter3 + 1);
                                            user.setPreval("2");
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 4);
                                        }
                                    }
                                } else if (pv.equalsIgnoreCase("3")) {
                                    Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                    if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                        Map<String, Object> checkpinfinal = checkPinU(msisdn, message);
                                        if (checkpinfinal.get("success").toString().equalsIgnoreCase("01")) {
                                            String menuElements = getValueByKey("enter_newpin", labels)[1];
                                            map.put("message", menuElements);
                                            user.setOldpin(hash(message));
                                            user.setPos("5");
                                            user.setMenulevel("1");// keep it to menu message
                                            usersRepo.save(user);
                                            map.put("command", 5);
                                        }
                                    } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                        if (max1 == iter3) {
                                            map.put("message", verif.get("result").toString());
                                            map.put("command", 0);
                                        } else {
                                            String lastms = verif.get("textformat").toString();
                                            map.put("message", lastms);
                                            user.setPos("4");
                                            user.setIteratorPIN(iter3 + 1);
                                            user.setPreval("3");
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 4);
                                        }
                                    }
                                }
                            }
                        }

                    } else if (pos.equalsIgnoreCase("5")) {
                        String sl = user.getSublevel();
                        String pv = user.getPreval();
                        if (sl.equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 0);

                            } else if (message.equalsIgnoreCase("7777")) {
                                List<Ftsltranspage> trans = ftsltranspage.findActive()
                                        .stream()
                                        .sorted(Comparator.comparing(Ftsltranspage::getRang))
                                        .collect(Collectors.toList());

                                String menuElements = getValueByKey("select_acc_credit", labels)[1];
                                for (Ftsltranspage element : trans) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                text = menuElements;
                                map.put("message", text);
                                user.setPos("3");
                                user.setPreval("1");
                                user.setMenulevel("1");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 3);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            } else {
                                if (pv.equalsIgnoreCase("1")) {
                                    Map<String, Object> verif = amounttest(message, max1, iter2);
                                    if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                        String menuElements = getValueByKey("ref_msg", labels)[1];
                                        map.put("message", menuElements);
                                        user.setPos("6");
                                        user.setAmount(message);
                                        user.setMenulevel("1");
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
                                            user.setIterator(iter2 + 1);
                                            user.setPreval("1");
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 5);
                                        }
                                    }
                                } else if (pv.equalsIgnoreCase("2")) {

                                    Map<String, Object> verif = amounttest(message, max1, iter2);
                                    if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                        String menuElements = getValueByKey("ref_msg", labels)[1];
                                        map.put("message", menuElements);
                                        user.setPos("6");
                                        user.setAmount(message);
                                        user.setMenulevel("1");
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
                                            user.setIterator(iter2 + 1);
                                            user.setPreval("1");
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 5);
                                        }
                                    }
                                }
                            }

                        } else if (sl.equalsIgnoreCase("3")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);

                            } else if (message.equalsIgnoreCase("7777")) {
                                List<Loans> DB = loanRepo.findActive();
                                System.out.println("::::::::::::::rynnnnnn fabri" + DB);
                                String menuElements = getValueByKey("menu_head", labels)[1];
                                for (Loans element : DB) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                map.put("message", menuElements);
                                user.setPos("3");
                                user.setPreval("3");
                                user.setMenulevel("1");
                                user.setSublevel("3");
                                usersRepo.save(user);
                                map.put("command", 3);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            } else {
                                if (pv.equalsIgnoreCase("1")) {
                                    String menuElements = getValueByKey("loan_finilisation", labels)[1];
                                    String LOAN = getValueByKey("exit_msg", labels)[1];
                                    map.put("message", menuElements + LOAN);
                                    user.setPos("6");
                                    user.setMotif(message);
                                    user.setMenulevel("1");
                                    usersRepo.save(user);
                                    map.put("command", 6);

                                } else if (pv.equalsIgnoreCase("2")) {
                                    String menuElements = getValueByKey("loan_finilisation", labels)[1];
                                    String LOAN = getValueByKey("exit_msg", labels)[1];
                                    map.put("message", menuElements + LOAN);
                                    user.setPos("6");
                                    user.setMotif(message);
                                    user.setMenulevel("1");
                                    usersRepo.save(user);
                                    map.put("command", 6);
                                }
                            }
                        } else if (sl.equalsIgnoreCase("4")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);

                            } else if (message.equalsIgnoreCase("7777")) {
                                List<Myaccount> ls = myaccountRepository.findActive();
                                String menuElements = getValueByKey("acc_manag_page", labels)[1];
                                for (Myaccount element : ls) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                map.put("message", menuElements);
                                user.setPos("3");
                                user.setPreval("4");
                                user.setMenulevel("1");
                                user.setSublevel("4");
                                usersRepo.save(user);
                                map.put("command", 3);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            } else {
                                if (pv.equalsIgnoreCase("3")) {
                                    Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                    if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                        String menuElements = getValueByKey("confirm_newpin", labels)[1];
                                        map.put("message", menuElements);
                                        user.setNewpin(hash(message));
                                        user.setPos("6");
                                        user.setMenulevel("1");// keep it to menu message
                                        usersRepo.save(user);
                                        map.put("command", 6);

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
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 5);
                                        }
                                    }
                                }
                            }
                        }

                    } else if (pos.equalsIgnoreCase("6")) {
                        String sl = user.getSublevel();
                        String pv = user.getPreval();
                        if (sl.equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);

                            } else if (message.equalsIgnoreCase("7777")) {
                                List<Ftsltranspage> trans = ftsltranspage.findActive()
                                        .stream()
                                        .sorted(Comparator.comparing(Ftsltranspage::getRang))
                                        .collect(Collectors.toList());

                                String menuElements = getValueByKey("select_acc_credit", labels)[1];
                                for (Ftsltranspage element : trans) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                text = menuElements;
                                map.put("message", text);
                                user.setPos("3");
                                user.setPreval("1");
                                user.setMenulevel("1");
                                user.setSublevel("1");
                                usersRepo.save(user);
                                map.put("command", 3);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            } else {
                                if (pv.equalsIgnoreCase("1")) {
                                    String menuElements = getValueByKey("enter_pin_confirm_trans", labels)[1];
                                    for (Back_home element : backmenu) {
                                        menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                    }
                                    if (pv.equalsIgnoreCase("1")) {
                                        String sdk = getValueByKey("check_info", labels)[1];
                                        Map<String, Object> resp = new HashMap<>();
                                        resp.put("telephone", num);
                                        System.out.println("::::::::::::::::: ace made the payment" + menuElements);
                                        Map<String, String> info = getwalletinqFTSL(resp);
                                        if (info.get("status").equalsIgnoreCase("01")) {
                                            map.put("message", sdk + amt+     "to"   + info.get("name") + menuElements);
                                            user.setPos("7");
                                            user.setMenulevel("1");// keep it to menu message
                                            usersRepo.save(user);
                                            map.put("command", 7);
                                        }
                                    }
                                } else if (pv.equalsIgnoreCase("2")) {
                                    String menuElements = getValueByKey("enter_pin_confirm_trans", labels)[1];
                                    for (Back_home element : backmenu) {
                                        menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                    }
                                    if (pv.equalsIgnoreCase("1")) {
                                        String sdk = getValueByKey("check_info", labels)[1];
                                        Map<String, Object> resp = new HashMap<>();
                                        resp.put("telephone", num);
                                        System.out.println(":::::::::::::ace made the payment" + menuElements);
                                        Map<String, String> info = getwalletinqFTSL(resp);
                                        if (info.get("status").equalsIgnoreCase("01")) {
                                            map.put("message", sdk + amt + "to" + info.get("name") + menuElements);
                                            user.setPos("7");
                                            user.setMenulevel("1");// keep it to menu message
                                            usersRepo.save(user);
                                            map.put("command", 7);


                                        }
                                    }
                                }
                            }
                        }
                        if (sl.equalsIgnoreCase("4")) {
                            if (message.equalsIgnoreCase("9999")) {
                                String menu_elements = this.getValueByKey("homepage", labels)[1];
                                for (Homepage elements : layout) {
                                    int va = elements.getRang();
                                    menu_elements = menu_elements + "\n" + va + " : " + elements.getValen();
                                }
                                text = menu_elements;
                                map.put("message", text);
                                user.setPos("1");
                                user.setMenulevel("1");
                                usersRepo.save(user);
                                map.put("command", 1);

                            } else if (message.equalsIgnoreCase("7777")) {
                                List<Myaccount> ls = myaccountRepository.findActive();
                                String menuElements = getValueByKey("acc_manag_page", labels)[1];
                                for (Myaccount element : ls) {
                                    menuElements += "\n" + element.getRang() + " : " + element.getValen();
                                }
                                map.put("message", menuElements);
                                user.setPos("3");
                                user.setPreval("4");
                                user.setMenulevel("1");
                                user.setSublevel("4");
                                usersRepo.save(user);
                                map.put("command", 3);

                            } else if (message.equalsIgnoreCase("99")) {
                                String menuElements = getValueByKey("exit_msg", labels)[1];
                                map.put("message", menuElements);
                                map.put("command", 0);

                            } else {
                                String op = user.getOldpin();
                                String np = user.getNewpin();
                                if (pv.equalsIgnoreCase("3")) {
                                    System.out.println("::::::::::::::dd.ace recuperation en local");
                                    Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                                    System.out.println("::::::::::::::dd.ace recuperation en local");
                                    if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                        System.out.println("::::::::::dd.ace recuperation en local" + verif);
                                        Map<String, String> comp = comparator(hash(message), np);
                                        System.out.println(":::::::::dd.ace recuperation en local" + comp);
                                        if (comp.get("message").equalsIgnoreCase("ok")) {
                                            /***
                                             * ADD THE API SERVICE FOR PIN MODIFICATION
                                             */
                                           EditPinDto resp = EditPinDto.builder()
                                                   .oldpin(op)
                                                   .newpin(np)
                                                   .tel(msisdn)
                                                   .build();
                                            Map<String, Object>  modifpin = changePin(resp);

                                        } else if (comp.get("message").equalsIgnoreCase("ko")) {
                                            String menuElements = getValueByKey("newpin_notmatch", labels)[1];
                                            map.put("message", menuElements);
                                            user.setPos("3");
                                            user.setPreval("2");
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 7);
                                        }
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
                                            user.setMenulevel("1");
                                            usersRepo.save(user);
                                            map.put("command", 6);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (pos.equalsIgnoreCase("7")) {
                        String sl = user.getSublevel();
                        String pv = user.getPreval();
                        if (sl.equalsIgnoreCase("1")) {
                            Map<String, Object> verif = pinverifiaction(message, max1, iter3);
                            Map<String, Object> checkipinfinal = checkPinU(msisdn, message);
                            Map<String, Object> resp= new HashMap<>();
                            resp.put("telephone",msisdn);
                            resp.put("pin",hash(message));
                            resp.put("member",member);
                            resp.put("codewalop",num);
                            resp.put("amount",amt);
                            resp.put("nat",nat);
                            resp.put("region",RegionEnum.LOCAL);
                            Map<String, Object>  processpaymt = requestPaiement(resp);
                            System.out.println("ace made the payment" + resp);
                            if (verif.get("result").toString().equalsIgnoreCase("ok")) {
                                if (processpaymt.get("success").toString().equalsIgnoreCase("01")) {
                                    map.put("response", "transaction successfull");
                                    map.put("message", processpaymt.get("message"));
                                    map.put("command", 8);
                                } else if (processpaymt.get("success").toString().equalsIgnoreCase("100")) {
                                    map.put("message", processpaymt.get("data"));
                                }
                            } else if (verif.get("result").toString().equalsIgnoreCase("ok1")) {
                                if (max1 == iter3) {
                                    map.put("message", verif.get("result").toString());
                                    map.put("command", 0);
                                } else {
                                    String lastms = verif.get("textformat").toString();
                                    map.put("message", lastms);
                                    user.setPos("6");
                                    user.setIteratorPIN(iter3 + 1);
                                    user.setPreval("4");
                                    user.setMenulevel("1");
                                    usersRepo.save(user);
                                    map.put("command", 7);
                                }
                            }
                        }
                    }

                    /***
                     * THIS IS THE GIMAC MENU LEVEL
                     */

                } else if (ml.equalsIgnoreCase("2")) {
                    List<PartnerCmBk> prtbk = partnerCmBkRepo.findActive()
                            .stream()
                            .sorted(Comparator.comparing(PartnerCmBk::getRang))
                            .collect(Collectors.toList());

                    List<PartnerCmW> wal = partnerCmWRepo.findActive()
                            .stream()
                            .sorted(Comparator.comparing(PartnerCmW::getRang))
                            .collect(Collectors.toList());

                    List<Country> ctymn = countryRepo.findActive()
                            .stream()
                            .sorted(Comparator.comparing(Country::getRang))
                            .collect(Collectors.toList());

                    if (pos.equalsIgnoreCase("1")) {
                        String menuElements = getValueByKey("gimachomepage", labels)[1];
                        for (gimacpage element : sortedMenu) {
                            menuElements += "\n" + element.getRang() + " : " + element.getValfr();
                        }
                        text = menuElements;
                        map.put("message", text);
                        user.setPos("2");
                        user.setMenulevel("2");
                        usersRepo.save(user);
                        map.put("command", 2);

                        /***
                         * THIS IS FOR COUNTRIES
                         */

                    } else if (pos.equalsIgnoreCase("2")) {
                        /***
                         * THIS IS FOR COUNTRIES MONEY TRANSFER
                         */
                        if (message.equalsIgnoreCase("1")) {
                            String menuElements = getValueByKey("choose_country", labels)[1];
                            for (Country element : ctymn) {
                                menuElements += "\n" + element.getRang() + " : " + element.getValfr();
                            }
                            text = menuElements;
                            map.put("message", text);
                            user.setPos("3");
                            user.setPreval("1");
                            user.setSublevel("1");
                            user.setMenulevel("2");
                            usersRepo.save(user);
                            map.put("command", 3);

                            /***
                             * THIS IS FOR COUNTRIES BILL PAYMENT
                             */
                        } else if (message.equalsIgnoreCase("2")) {
                            String menuElements = getValueByKey("choose_country", labels)[1];
                            for (Country element : ctymn) {
                                menuElements += "\n" + element.getRang() + " : " + element.getValfr();
                            }
                            text = menuElements;
                            map.put("message", text);
                            user.setPos("3");
                            user.setPreval("2");
                            user.setSublevel("2");
                            user.setMenulevel("2");
                            usersRepo.save(user);
                            map.put("command", 3);

                            /***
                             * THIS IS FOR COUNTRIES MOBILE RELOAD
                             */
                        } else if (message.equalsIgnoreCase("3")) {
                            String menuElements = getValueByKey("choose_country", labels)[1];
                            for (Country element : ctymn) {
                                menuElements += "\n" + element.getRang() + " : " + element.getValfr();
                            }
                            text = menuElements;
                            map.put("message", text);
                            user.setPos("3");
                            user.setPreval("3");
                            user.setSublevel("3");
                            user.setMenulevel("2");
                            usersRepo.save(user);
                            map.put("command", 3);

                        } else if (message.equalsIgnoreCase("7777")) {
                            String menu_elements = this.getValueByKey("homepage", labels)[1];
                            for (Homepage elements : layout) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;
                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("1");
                            usersRepo.save(user);
                            map.put("command", 1);

                        } else if (message.equalsIgnoreCase("9999")) {
                            String menu_elements = this.getValueByKey("homepage", labels)[1];
                            for (Homepage elements : layout) {
                                int va = elements.getRang();
                                menu_elements = menu_elements + "\n" + va + " : " + elements.getValfr();
                            }
                            text = menu_elements;
                            map.put("message", text);
                            user.setPos("1");
                            user.setMenulevel("1");
                            usersRepo.save(user);
                            map.put("command", 1);

                        } else if (message.equalsIgnoreCase("99")) {
                            String menuElements = getValueByKey("exit_msg", labels)[1];
                            map.put("message", menuElements);
                            map.put("command", 0);
                        }

                        /***
                         * THIS IS FOR ACCOUNTS & WALLETS
                         */

                    } else if (pos.equalsIgnoreCase("3")) {
                        String sl = user.getSublevel();
                        if (sl.equalsIgnoreCase("1")) {
                            if (message.equalsIgnoreCase("1")) {
                                List<Cmaccount> acctype = cmaccRepo.findActive()
                                        .stream()
                                        .sorted(Comparator.comparing(Cmaccount::getRang))
                                        .collect(Collectors.toList());

                                String menuEl = getValueByKey("select_acc_credit", labels)[1];
                                for (Cmaccount ls : acctype) {
                                    menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                }
                                text = menuEl;
                                map.put("message", text);
                                user.setPos("4");
                                user.setPreval("1");
                                user.setMenulevel("2");
                                usersRepo.save(user);
                                map.put("command", 4);


                            } else if (message.equalsIgnoreCase("2")) {
                                    List<Cmaccount> acctype = cmaccRepo.findActive()
                                            .stream()
                                            .sorted(Comparator.comparing(Cmaccount::getRang))
                                            .collect(Collectors.toList());

                                    String menuEl = getValueByKey("select_acc_credit", labels)[1];
                                    for (Cmaccount ls : acctype) {
                                        menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                    }
                                    text = menuEl;
                                    map.put("message", text);
                                    user.setPos("4");
                                    user.setPreval("2");
                                    user.setMenulevel("2");
                                    usersRepo.save(user);
                                    map.put("command", 4);

                            } else if (message.equalsIgnoreCase("3")) {
                                List<Cmaccount> acctype = cmaccRepo.findActive()
                                        .stream()
                                        .sorted(Comparator.comparing(Cmaccount::getRang))
                                        .collect(Collectors.toList());

                                String menuEl = getValueByKey("select_acc_credit", labels)[1];
                                for (Cmaccount ls : acctype) {
                                    menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                }
                                text = menuEl;
                                map.put("message", text);
                                user.setPos("4");
                                user.setPreval("3");
                                user.setMenulevel("2");
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("4")) {
                                List<Cmaccount> acctype = cmaccRepo.findActive()
                                        .stream()
                                        .sorted(Comparator.comparing(Cmaccount::getRang))
                                        .collect(Collectors.toList());

                                String menuEl = getValueByKey("select_acc_credit", labels)[1];
                                for (Cmaccount ls : acctype) {
                                    menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                }
                                text = menuEl;
                                map.put("message", text);
                                user.setPos("4");
                                user.setPreval("4");
                                user.setMenulevel("2");
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("5")) {
                                List<Cmaccount> acctype = cmaccRepo.findActive()
                                        .stream()
                                        .sorted(Comparator.comparing(Cmaccount::getRang))
                                        .collect(Collectors.toList());

                                String menuEl = getValueByKey("select_acc_credit", labels)[1];
                                for (Cmaccount ls : acctype) {
                                    menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                }
                                text = menuEl;
                                map.put("message", text);
                                user.setPos("4");
                                user.setPreval("5");
                                user.setMenulevel("2");
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("6")) {
                                List<Cmaccount> acctype = cmaccRepo.findActive()
                                        .stream()
                                        .sorted(Comparator.comparing(Cmaccount::getRang))
                                        .collect(Collectors.toList());

                                String menuEl = getValueByKey("select_acc_credit", labels)[1];
                                for (Cmaccount ls : acctype) {
                                    menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                }
                                text = menuEl;
                                map.put("message", text);
                                user.setPos("4");
                                user.setPreval("6");
                                user.setMenulevel("2");
                                usersRepo.save(user);
                                map.put("command", 4);
                            }

                            /***
                             * THIS IS FOR BILL PAYMENT PARTNERS
                             */
                        } else if (sl.equalsIgnoreCase("2")) {
                            if (message.equalsIgnoreCase("1")) {
                                String menuEl = getValueByKey("choose_partner", labels)[1];
                                for (PartnerCmW ls : wal) {
                                    menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                }
                                text = menuEl;
                                map.put("message", text);
                                user.setPos("4");
                                user.setPreval("1");
                                user.setMenulevel("2");
                                usersRepo.save(user);
                                map.put("command", 4);

                            } else if (message.equalsIgnoreCase("2")) {
                                String menuEl = getValueByKey("out_of_service", labels)[1];
                                map.put("message", menuEl);
                                map.put("command", 0);

                            } else if (message.equalsIgnoreCase("3")) {
                                String menuEl = getValueByKey("out_of_service", labels)[1];
                                map.put("message", menuEl);
                                map.put("command", 0);

                            } else if (message.equalsIgnoreCase("4")) {
                                String menuEl = getValueByKey("out_of_service", labels)[1];
                                map.put("message", menuEl);
                                map.put("command", 0);

                            } else if (message.equalsIgnoreCase("5")) {
                                String menuEl = getValueByKey("out_of_service", labels)[1];
                                map.put("message", menuEl);
                                map.put("command", 0);

                            } else if (message.equalsIgnoreCase("6")) {
                                String menuEl = getValueByKey("out_of_service", labels)[1];
                                map.put("message", menuEl);
                                map.put("command", 0);

                            }


                            /***
                             * THIS IS FOR MOBILE RELOAD PARTNERS
                             */
                        } else if (sl.equalsIgnoreCase("3")) {
                            if (message.equalsIgnoreCase("1")) {

                            } else if (message.equalsIgnoreCase("2")) {

                            } else if (message.equalsIgnoreCase("3")) {

                            } else if (message.equalsIgnoreCase("4")) {

                            } else if (message.equalsIgnoreCase("5")) {

                            } else if (message.equalsIgnoreCase("6")) {

                            }


                        }
                    }

                    /***
                     * THIS IS FOR PARTNERS CAMEROON
                     */

                    if (pos.equalsIgnoreCase("4")) {
                        String pv = user.getPreval();
                        String sl = user.getSublevel();
                        if (sl.equalsIgnoreCase("1")) {
                            if (pv.equalsIgnoreCase("1")) {
                                if (message.equalsIgnoreCase("1")) {
                                    String menuEl = getValueByKey("choose_partner", labels)[1];
                                    for (PartnerCmW ls : wal) {
                                        menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                    }
                                    text = menuEl;
                                    map.put("message", text);
                                    user.setPos("5");
                                    user.setPreval2("1");
                                    user.setMenulevel("2");
                                    usersRepo.save(user);
                                    map.put("command", 5);

                                } else if (message.equalsIgnoreCase("2")) {
                                    if (message.equalsIgnoreCase("1")) {
                                        String menuEl = getValueByKey("choose_partner", labels)[1];
                                        for (PartnerCmBk ls : prtbk) {
                                            menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                        }
                                        text = menuEl;
                                        map.put("message", text);
                                        user.setPos("5");
                                        user.setPreval2("2");
                                        user.setMenulevel("2");
                                        usersRepo.save(user);
                                        map.put("command", 5);
                                    }
                                }

                            }else if (pv.equalsIgnoreCase("2")) {
                                if (message.equalsIgnoreCase("1")) {
                                    String menuEl = getValueByKey("choose_partner", labels)[1];
                                    for (PartnerCmW ls : wal) {
                                        menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                    }
                                    text = menuEl;
                                    map.put("message", text);
                                    user.setPos("5");
                                    user.setPreval2("1");
                                    user.setMenulevel("2");
                                    usersRepo.save(user);

                                } else if (message.equalsIgnoreCase("2")) {
                                    if (message.equalsIgnoreCase("1")) {
                                        String menuEl = getValueByKey("choose_partner", labels)[1];
                                        for (PartnerCmBk ls : prtbk) {
                                            menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                        }
                                        text = menuEl;
                                        map.put("message", text);
                                        user.setPos("5");
                                        user.setPreval2("2");
                                        user.setMenulevel("2");
                                        usersRepo.save(user);
                                    }
                                }
                            }else if (pv.equalsIgnoreCase("3")) {
                                if (message.equalsIgnoreCase("1")) {
                                    String menuEl = getValueByKey("choose_partner", labels)[1];
                                    for (PartnerCmW ls : wal) {
                                        menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                    }
                                    text = menuEl;
                                    map.put("message", text);
                                    user.setPos("5");
                                    user.setPreval2("1");
                                    user.setMenulevel("2");
                                    usersRepo.save(user);

                                } else if (message.equalsIgnoreCase("2")) {
                                    if (message.equalsIgnoreCase("1")) {
                                        String menuEl = getValueByKey("choose_partner", labels)[1];
                                        for (PartnerCmBk ls : prtbk) {
                                            menuEl += "\n" + ls.getRang() + " : " + ls.getValfr();
                                        }
                                        text = menuEl;
                                        map.put("message", text);
                                        user.setPos("5");
                                        user.setPreval2("2");
                                        user.setMenulevel("2");
                                        usersRepo.save(user);
                                    }
                                }
                            } else if (pv.equalsIgnoreCase("4")) {

                            }


                        } else if (sl.equalsIgnoreCase("2")) {

                        }
                    }
                }

            } else if (user.getLanguage() != null && user.getLanguage().equalsIgnoreCase("0")) {

            }

        } else {
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
        return testRegularExpression3("^\\d+(\\.\\d+)?$", amount, max, iter);
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

    private Map<String, Object> testRegularExpression3(String regex, String amount, int maxTrial, int iter) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(amount);
        Map<String, Object> res = new HashMap<>();

        if (iter < maxTrial + 1) {
            if (matcher.matches()) {
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

    public Map<String, String> getsolde(@RequestBody Map<String, Object> payload) {
        Map<String, Object> obj = ussdFirstTrustService.getCpt1(payload);
        Map<String, Object> obj1 = ussdFirstTrustService.getCli(payload);
        System.out.println(obj);
        return ussdFirstTrustService.getSolde(obj1.get("cli").toString(), obj.get("cpt").toString());
    }

    @RequestMapping(value = "/requestpayment1", method = RequestMethod.POST)
    public Map<String, Object> requestPaiement(@RequestBody Map<String,Object> payload) {
        Map<String, Object> object = ussdFirstTrustService.addListPaiement(payload);
        return object;
    }

    @RequestMapping(value = "/getwalletinq21", method = RequestMethod.POST)
    public Map<String, Object> getwalletinq2(@RequestBody Map<String, Object> payload) {
        Map<String, Object> Object =ussdFirstTrustService.walletinq(payload);
        System.out.println();
        return Object;
    }

    @RequestMapping(value = "/getwalletinquiry1", method = RequestMethod.POST)
    public Map<String, String> getwalletinqFTSL(@RequestBody Map<String, Object> payload) {
        Map<String, Object> obj = ussdFirstTrustService.getCpt1(payload);
        Map<String, Object> obj1 = ussdFirstTrustService.getCli(payload);
        System.out.println(obj);
        return ussdFirstTrustService.walletinquiryftsl(obj1.get("cli").toString(), obj.get("cpt").toString());
    }

    @RequestMapping(value = "/billInquiry1", method = RequestMethod.POST)
    public Map<String, Object> billinquiry(@RequestBody Map<String, Object> payload) {
        Map<String, Object> Obj = ussdFirstTrustService.billfunc(payload);
        Map<String, Object> Object = ussdFirstTrustService.billdetails(Obj);
        return Object;
    }

    @RequestMapping(value = "/mobilereload1", method = RequestMethod.POST)
    public Map<String, Object> EtopUp(@RequestBody Map<String, Object> payload) {
        Map<String, Object> Object = ussdFirstTrustService.airtimereload(payload);
        return Object;
    }

    @RequestMapping(value = "/billpayment1", method = RequestMethod.POST)
    public Map<String, Object> paybill(@RequestBody Map<String, Object> payload) {
        Map<String, Object> Obj = billinquiry(payload);
        return ussdFirstTrustService.billpayment(Obj);
    }

    @RequestMapping(value = "/request_to_pay", method = RequestMethod.POST)
    public Map<String, Object> requesttopay(@RequestBody Map<String, Object> payload) {
        Map<String, Object> Obj = ussdFirstTrustService.requesttopay(payload);
        return Obj;
    }
    @RequestMapping(value = "/cardless_withdrawal", method = RequestMethod.POST)
    public Map<String, Object> cardless_withdrawal(@RequestBody Map<String, Object> payload) {
        Map<String, Object> Obj = ussdFirstTrustService.cardless_with(payload);
        return Obj;
    }

    @RequestMapping(value = "/checkPinU1", method = RequestMethod.POST)
    public Map<String, Object> checkPinU(@RequestBody String tel, String pinuser) {
        String pin = hash(pinuser);
        return ussdFirstTrustService.CheckPin(tel, pin);
    }

    @RequestMapping(value = "/getNomencTabcdAcscd1", method = RequestMethod.GET)
    public ArrayList<Nomenclature> getNomencTabcdAcscd () throws Exception {
        System.out.println(":::::::::::::::::: yvo recuperation en local: getNomencTabcd :::::::::::::::");
        return ussdFirstTrustService.getNomencTabcdAcscd();
    }

    @RequestMapping(value = "/changePin", method = RequestMethod.POST)
    public Map<String, Object>  changePin(@RequestBody EditPinDto payload) {
        return ussdFirstTrustService.editpin(payload);
    }

    @RequestMapping(value = "/getCli1", method = RequestMethod.POST)
    public Map<String, Object> getcli(@RequestBody Map<String, Object> telephone) {
        return ussdFirstTrustService.getCli(telephone);
    }

    @RequestMapping(value = "/getCpt1", method = RequestMethod.POST)
    public Map<String, Object> getcpt(@RequestBody Map<String, Object> telephone) {
        return ussdFirstTrustService.getCpt1(telephone);
    }



    public Map<String, String> comparator(String a, String b) {
        String s1 = a;
        String s2 = b;
        Map<String, String> res = new HashMap<>();
        if (s1.equals(s2)) {
            String result = "ok";
            res.put("message", result);
        } else {
            String result = "ko";
            res.put("message", result);
        }
        return res;
    }

    public String hash(String pin) {
        System.out.println(":::::::: this is the pin ::::::: " + pin);
        return ussdFirstTrustService.generateHash(pin);
    }

    public Map<String, Object> checkPayload(Map<String, Object> payload, String key) {
        if (!payload.containsKey(key)) {
            payload.put(key, "");
        }
        return payload;
    }

}
