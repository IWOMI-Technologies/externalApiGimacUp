package com.iwomi.External_api_cccNewUp.service;

import com.iwomi.External_api_cccNewUp.Dto.UssdPayloadDTO;
import com.iwomi.External_api_cccNewUp.Entities.Nomenclature;
import com.iwomi.External_api_cccNewUp.Entities.UserSession;
import com.iwomi.External_api_cccNewUp.repo.NomenclatureRepository;
import com.iwomi.External_api_cccNewUp.repo.UserSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UssdService {
    @Autowired
    UserSessionRepo usersRepo;
    @Autowired
    NomenclatureRepository nomenclatureRepo;

    public HashMap<String, Object> createSessionAndReturnHomeMenu(UssdPayloadDTO payload) {
        var response = new HashMap<String, Object>();

        System.out.println(":::::::::::: New Session Started by User :::::::::::: ");

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
                .pos("000000")
                .build();

        var bdSession = usersRepo.save(newSession);

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

        for (int i = -0; i < nomenclatures.size(); i++) {
            var label = nomenclatures.get(i).getLib1();

            text = i == 0 ? text + label : text + "\n" + label;
        }

        return text;
    }
}
