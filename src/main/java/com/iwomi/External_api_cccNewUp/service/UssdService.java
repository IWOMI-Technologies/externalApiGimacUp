package com.iwomi.External_api_cccNewUp.service;

import com.iwomi.External_api_cccNewUp.Core.constants.Menu;
import com.iwomi.External_api_cccNewUp.Dto.UssdPayloadDTO;
import com.iwomi.External_api_cccNewUp.Entities.Nomenclature;
import com.iwomi.External_api_cccNewUp.Entities.UserSession;
import com.iwomi.External_api_cccNewUp.repo.NomenclatureRepository;
import com.iwomi.External_api_cccNewUp.repo.UserSessionRepo;
import oracle.ucp.util.Pair;
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
                .menulevel("0")
                .uuid(payload.sessionid)
                .phone(payload.msisdn)
                .max(3)
                .iteratorPIN(1)
                .iteratorAMT(1)
                .iterator(1)
                .provider(payload.provider)
                .lang("1")
                .pos(Menu.StartLevel)
                .build();

        var bdSession = sessionRepo.save(newSession);

        System.out.println(":::::::::::: Session Saved in BD :::::::::::: " + bdSession);

        var nomenclatures = nomenclatureRepo.getHomePageMenu("9090", "1");

        System.out.println(":::::::::::: Home Page Menus :::::::::::: " + nomenclatures);

        var text = constructMenuFromNomens(nomenclatures);

        response.put("message", text);
        response.put("command", 1);

        return response;
    }

    public String constructMenuFromNomens(List<Nomenclature> nomenclatures) {
        var text = "";

        if (nomenclatures.isEmpty()) return "Oops";

        for (int i = 0; i < nomenclatures.size(); i++) {
            var label = nomenclatures.get(i).getLib1();
            text = i == 0 ? text + label : text + "\n" + label;
        }

        if (nomenclatures.get(0).getLib8().equalsIgnoreCase(Menu.StartLevel)) return text;

        text = text + "\n------\n" + "7777. Previous\n9999. Home";

        return text;
    }


    public Pair<String, HashMap<String, Object>> goPrevMenu(String currentLevel) {
        var currentMenus = nomenclatureRepo.findTabcdAndLevel(Menu.Tabcd, currentLevel);

        log.info(":::::::::::: currentMenus :::::::::::: " + currentMenus);

        if (currentMenus == null || currentMenus.isEmpty()) {
            log.info(":::::::::::: NO MENU FOUND :::::::::::: ");

            var response = new HashMap<String, Object>();
            response.put("message", "Picked Menu Not Found.");
            response.put("command", 1);
            return new Pair<>(Menu.StartLevel, response);
        }

        var prevLevel = currentMenus.get(0).getLib6();
        var res = getMenuItems(prevLevel);

        return new Pair<>(prevLevel, res);
    }

    public Pair<String, HashMap<String, Object>> goToUserChooseMenu(String currentLevel, String userInput) {
        var response = new HashMap<String, Object>();
        log.info(":::::::::::: User Input :::::::::::: " + userInput);

        var pickedMenu = nomenclatureRepo.findTabcdAndLevelAndRang(Menu.Tabcd, currentLevel, userInput);

        if (pickedMenu == null) {
            response.put("message", "Picked Menu Not Found.");
            response.put("command", 1);
            return new Pair<>(Menu.StartLevel, response);
        }

        var nextMenus = nomenclatureRepo.findTabcdAndLevel(Menu.Tabcd, pickedMenu.getLib5());

        log.info(":::::::::::: NEXT MENU :::::::::::: " + nextMenus);

        response.put("message", constructMenuFromNomens(nextMenus));
        response.put("command", 1);

        return new Pair<>(pickedMenu.getLib5(), response);
    }

    public HashMap<String, Object> getMenuItems(String nextLevel) {
        var response = new HashMap<String, Object>();
        var nextMenus = nomenclatureRepo.findTabcdAndLevel(Menu.Tabcd, nextLevel);

        log.info(":::::::::::: NEXT MENU :::::::::::: " + nextMenus);

        response.put("message", constructMenuFromNomens(nextMenus));
        response.put("command", 1);

        return response;
    }
}
