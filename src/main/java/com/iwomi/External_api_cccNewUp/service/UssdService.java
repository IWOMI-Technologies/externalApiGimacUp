package com.iwomi.External_api_cccNewUp.service;

import com.google.gson.Gson;
import com.iwomi.External_api_cccNewUp.Core.constants.AppString;
import com.iwomi.External_api_cccNewUp.Core.constants.Menu;
import com.iwomi.External_api_cccNewUp.Core.constants.NomenclatureTables;
import com.iwomi.External_api_cccNewUp.Dto.TransferInfo;
import com.iwomi.External_api_cccNewUp.Dto.UssdPayloadDTO;
import com.iwomi.External_api_cccNewUp.Entities.Nomenclature;
import com.iwomi.External_api_cccNewUp.Entities.UserSession;
import com.iwomi.External_api_cccNewUp.repo.NomenclatureRepository;
import com.iwomi.External_api_cccNewUp.repo.UserSessionRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class UssdService {
    @Autowired
    UserSessionRepo sessionRepo;
    @Autowired
    NomenclatureRepository nomenclatureRepo;
    @Autowired
    ApiClient apiClient;
    private final Log log = LogFactory.getLog(UssdService.class);
    private final Gson gson = new Gson();

    public HashMap<String, Object> createSessionAndReturnHomeMenu(UssdPayloadDTO payload) {
        var response = new HashMap<String, Object>();

        log.info(":::::::::::: SESSION NOT PRESENT. CREATING NEW SESSION :::::::::::: ");

        UserSession newSession = UserSession.builder()
                .menuLevel("0").uuid(payload.sessionid)
                .phone(payload.msisdn)
                .max(3)
                .provider(payload.provider)
                .language("en")
                .pos(Menu.StartLevel)
                .build();

        var bdSession = sessionRepo.save(newSession);
        log.info(":::::::::::: Session Saved in BD :::::::::::: " + bdSession);

        var nomenclatures = nomenclatureRepo.getHomePageMenu("9090", "1");
        log.info(":::::::::::: Home Page Menus :::::::::::: " + nomenclatures);

        if (nomenclatures.isEmpty()) {
            response.put("message", "Contact Admin.");
            response.put("command", 1);
            return response;
        }

        response.put("message", constructMenuFromNomens(nomenclatures, "", ""));
        response.put("command", 1);

        return response;
    }

    public HashMap<String, Object> goToUserChooseMenu(String currentLevel, String userInput, UserSession session) {
        List<Nomenclature> currentMenus; // Menu Gotten from the last position stored in the session.
        Nomenclature userPickedMenu; // Previous menu form [currentMenus] gotten from the lib6 of [currentMenus].
        List<Nomenclature> pickedMenuContent; // Next menu form [userPickedMenu] gotten from the lib5 of [userPickedMenu].

        currentMenus = nomenclatureRepo.findTabcdAndLevel(Menu.Tabcd, session.getPos());

        if (currentMenus != null && currentMenus.size() == 1) {
            // In case we have only a single menu, we get the only menu.
            userPickedMenu = currentMenus.get(0);
        } else {
            // We get the menu based on user input
            userPickedMenu = nomenclatureRepo.findTabcdAndLevelAndRang(Menu.Tabcd, currentLevel, userInput);
        }

        if (userPickedMenu == null) throw new IllegalArgumentException("Picked Menu Not Found.");

        pickedMenuContent = nomenclatureRepo.findTabcdAndLevel(Menu.Tabcd, userPickedMenu.getLib5());

        session.setPos(userPickedMenu.getLib5());// Set the current user position

        log.info("::::::::: CURRENT MENU ::::::::::::" + userPickedMenu);
        log.info("::::::::: NEXT MENU :::::::::::: " + pickedMenuContent);

        var prefixText = "";
        var additionalTxt = "";

        var output = "";
        var store = userPickedMenu.getLib9();

        if (pickedMenuContent.size() == 1) {
            output = pickedMenuContent.get(0).getLib10();
        }

        if (store != null && !store.isEmpty()) {
            switch (store) {
                case AppString.WALLET:
                    session.setWallet(userInput);
                    break;
                case AppString.TYPE:
                    session.setNat(userInput);
                    break;
                case AppString.ACCOUNT:
                    session.setAcc(userInput);
                    break;
                case AppString.AMOUNT:
                    session.setAmount(userInput);
                    break;
                case AppString.MEMBER:
                    var index = Integer.parseInt(userInput);
                    var operator = getOperatorByCountryCode(session.getCountry(), index);
                    session.setMember(operator.getLib2());
                    break;
                case AppString.REFERENCE:
                    var info = getTransferDetail(session.amount, session.wallet);
                    prefixText = constructMessage(info, session.amount, session.wallet);
                    session.setRef(userInput);
                    break;
                case AppString.PIN:
                    session.setPin(userInput);
                    break;
            }
        }

        if (output != null && !output.isEmpty()) {
            additionalTxt = switch (output) {
                case AppString.CTRY -> {
                    var countries = getCountries().stream().map(Nomenclature::getLib2).toList();
                    yield formatEntries(countries);
                }
                case AppString.OPE -> {
                    var countries = getCountries();
                    var index = Integer.parseInt(userInput);
                    session.setCountry(countries.get(index - 1).getLib1());
                    yield formatEntries(getOperatorsByIndex(index, countries));
                }
                default -> additionalTxt;
            };
        }

        var response = new HashMap<String, Object>();
        response.put("message", constructMenuFromNomens(pickedMenuContent, additionalTxt, prefixText));
        response.put("command", 1);

        sessionRepo.save(session);

        return response;
    }


    public HashMap<String, Object> goPrevMenu(String currentLevel, UserSession session) {
        var currentMenus = nomenclatureRepo.findTabcdAndLevel(Menu.Tabcd, currentLevel);

        log.info(":::::::::::: currentMenus :::::::::::: " + currentMenus);

        if (currentMenus == null || currentMenus.isEmpty()) {
            log.info(":::::::::::: NO MENU FOUND :::::::::::: ");

            var response = new HashMap<String, Object>();
            response.put("message", "Picked Menu Not Found.");
            response.put("command", 1);
            return response;
        }

        var prevLevel = currentMenus.get(0).getLib6();
        var res = getMenuItems(prevLevel);

        session.setPos(currentMenus.get(0).getLib6());
        sessionRepo.save(session);

        return res;
    }

    public List<Nomenclature> getCountries() {
        var nomen = nomenclatureRepo.findTabcdAndDel(NomenclatureTables.CountriesTabcd, "0");

        if (nomen == null || nomen.isEmpty()) return new ArrayList<>();

        var countries = nomen.stream().filter((e) -> !Objects.equals(e.getLib3(), "1")).toList();

        log.info(":::::::::::: Countries :::::::::::: " + countries);

        return countries;
    }

    public List<String> getOperatorsByIndex(int index, List<Nomenclature> countries) {
        var country = countries.get(index - 1);

        var operators = nomenclatureRepo.findTabcdAndDel(NomenclatureTables.OperatorTabcd, "0");

        if (operators == null || operators.isEmpty()) return new ArrayList<>();

        var operatorsAsString = operators.stream().filter((e) -> Objects.equals(e.getLib1(), country.getLib1()))
                .map(Nomenclature::getLib2).toList();

        log.info(":::::::::::: operatorsAsString :::::::::::: " + operatorsAsString);

        return operatorsAsString;
    }

    public Nomenclature getOperatorByCountryCode(String code, int index) {
        var operators = nomenclatureRepo.findTabcdAndLib1AndDel(NomenclatureTables.OperatorTabcd, code, "0");

        if (operators == null || operators.isEmpty()) return null;

        var operator = operators.get(index - 1);

        log.info(":::::::::::: operator :::::::::::: " + operator);

        return operator;
    }

    public String formatEntries(List<String> list) {
        var additionalTxt = "";

        for (int i = 0; i < list.size(); i++) {
            var label = list.get(i);
            var pos = (i + 1) + ". ";

            if (i == 0) {
                additionalTxt = additionalTxt + pos + label;
            } else {
                additionalTxt = additionalTxt + "\n" + pos + label;
            }
        }

        return additionalTxt;
    }

    public HashMap<String, Object> returnNoMenuFound(String message) {
        var response = new HashMap<String, Object>();

        response.put("message", message);
        response.put("command", 1);

        return response;
    }

    public HashMap<String, Object> getMenuItems(String nextLevel) {
        var response = new HashMap<String, Object>();
        var nextMenus = nomenclatureRepo.findTabcdAndLevel(Menu.Tabcd, nextLevel);

        log.info(":::::::::::: NEXT MENU :::::::::::: " + nextMenus);

        response.put("message", constructMenuFromNomens(nextMenus, "", ""));
        response.put("command", 1);

        return response;
    }

    public String constructMenuFromNomens(List<Nomenclature> nomenclatures, String additionalText, String prefixText) {
        if (nomenclatures.isEmpty()) return "Couldn't get next menus";

        var text = "";

        for (int i = 0; i < nomenclatures.size(); i++) {
            var label = nomenclatures.get(i).getLib1();
            text = i == 0 ? text + label : text + "\n" + label;
        }

        if (nomenclatures.get(0).getLib8().equalsIgnoreCase(Menu.StartLevel)) return text;

        return prefixText + "\n" + text + additionalText + "\n------\n" + Menu.PrevMenu + ". Previous\n" + Menu.HomeMenu + ". Home";
    }

    public String constructMenuFromText(String message, String additionalText) {
        return message + additionalText + "\n------\n" + Menu.PrevMenu + ". Previous\n" + Menu.HomeMenu + ". Home";
    }

    public Boolean isBankWallet(String wallet) {
        try {
            var query = new HashMap<String, Object>();
            query.put("wallet", wallet);
            var result = apiClient.get("/exists-wallet", query, Boolean.class);

            return result.getStatus() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public String constructMessage(TransferInfo info, String amount, String wallet) {
        var str = "";

        if (info.can) {
            str = "You are about to transfer " + amount + " to " + wallet + "-" + info.fullName + ". Fees: " + info.fees;
        } else {
            str = info.reason + " to transfer " + amount + " to " + wallet + "-" + info.fullName + ". Fees: " + info.fees;
        }

        return str;
    }

    public TransferInfo getTransferDetail(String amount, String wallet) {
        var body = new HashMap<String, Object>();

        body.put("wallet", wallet);
        body.put("amount", amount);

        var result = apiClient.post("/transferDetail", new HashMap<String, Object>(), body);

        System.out.println("::::::::: RESULT  :::::::::::: " + result);

        if (result.getStatus() == 200) {
            var info = gson.fromJson(result.getBody().toString(), TransferInfo.class);
            System.out.println("::::::::: TransferInfo  :::::::::::: " + info);

            return info;
        }

        return null;
    }
}
