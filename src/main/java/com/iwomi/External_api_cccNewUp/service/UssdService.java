package com.iwomi.External_api_cccNewUp.service;

import com.iwomi.External_api_cccNewUp.Core.constants.Menu;
import com.iwomi.External_api_cccNewUp.Dto.UssdPayloadDTO;
import com.iwomi.External_api_cccNewUp.Entities.Nomenclature;
import com.iwomi.External_api_cccNewUp.Entities.UserSession;
import com.iwomi.External_api_cccNewUp.repo.NomenclatureRepository;
import com.iwomi.External_api_cccNewUp.repo.UserSessionRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UssdService {
    @Autowired
    UserSessionRepo sessionRepo;
    @Autowired
    NomenclatureRepository nomenclatureRepo;
    private static final Log log = LogFactory.getLog(UssdService.class);

    public HashMap<String, Object> createSessionAndReturnHomeMenu(UssdPayloadDTO payload) {
        var response = new HashMap<String, Object>();

        UserSession newSession = UserSession.builder()
                .menuLevel("0").uuid(payload.sessionid)
                .phone(payload.msisdn)
                .max(3).provider(payload.provider)
                .language("en")
                .pos(Menu.StartLevel)
                .build();

        var bdSession = sessionRepo.save(newSession);
        System.out.println(":::::::::::: Session Saved in BD :::::::::::: " + bdSession);

        var nomenclatures = nomenclatureRepo.getHomePageMenu("9090", "1");

        System.out.println(":::::::::::: Home Page Menus :::::::::::: " + nomenclatures);

        if (nomenclatures.isEmpty()) {
            response.put("message", "Contact Admin.");
            response.put("command", 1);
            return response;
        }

        response.put("message", constructMenuFromNomens(nomenclatures));
        response.put("command", 1);

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

    public HashMap<String, Object> goToUserChooseMenu(String currentLevel, String userInput, UserSession session) {
        var menus = nomenclatureRepo.findTabcdAndLevel(Menu.Tabcd, session.getPos());
        Nomenclature prevMenu;

        if (menus != null && menus.size() == 1) {
            prevMenu = menus.get(0);
        } else {
            prevMenu = nomenclatureRepo.findTabcdAndLevelAndRang(Menu.Tabcd, currentLevel, userInput);
        }

        if (prevMenu == null) throw new IllegalArgumentException("Picked Menu Not Found.");

        var nextMenus = nomenclatureRepo.findTabcdAndLevel(Menu.Tabcd, prevMenu.getLib5());

        log.info("::::::::: CURRENT MENU ::::::::::::" + prevMenu);
        log.info(":::::::::::: NEXT MENU :::::::::::: " + nextMenus);

        var type = prevMenu.getLib9();

        if (type != null && !type.isEmpty()) {
            switch (type) {
                case "WALLET":
                    session.setWallet(userInput);
                    break;
                case "REFERENCE":
                    session.setRef(userInput);
                    break;
                case "PIN":
                    session.setPin(userInput);
                    break;
            }
        }

        session.setPos(prevMenu.getLib5());
        sessionRepo.save(session);

        var response = new HashMap<String, Object>();

        response.put("message", constructMenuFromNomens(nextMenus));
        response.put("command", 1);

        return response;
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

        response.put("message", constructMenuFromNomens(nextMenus));
        response.put("command", 1);

        return response;
    }

    public String constructMenuFromNomens(List<Nomenclature> nomenclatures) {
        if (nomenclatures.isEmpty()) return "Couldn't get next menus";

        var text = "";

        for (int i = 0; i < nomenclatures.size(); i++) {
            var label = nomenclatures.get(i).getLib1();
            text = i == 0 ? text + label : text + "\n" + label;
        }

        if (nomenclatures.get(0).getLib8().equalsIgnoreCase(Menu.StartLevel)) return text;

        return text + "\n------\n" + Menu.PrevMenu + ". Previous\n" + Menu.HomeMenu + ". Home";
    }
}
