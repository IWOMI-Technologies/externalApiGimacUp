package com.iwomi.External_api_cccNewUp.controller;

import com.iwomi.External_api_cccNewUp.Dto.UssdPayloadDTO;
import com.iwomi.External_api_cccNewUp.repo.NomenclatureRepository;
import com.iwomi.External_api_cccNewUp.repo.UserSessionRepo;
import com.iwomi.External_api_cccNewUp.service.UssdService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("${apiPrefix}")
@Component
@RequiredArgsConstructor

public class UssdController {
    @Autowired
    UserSessionRepo sessionRepo;
    @Autowired
    UssdService ussdService;
    @Autowired
    NomenclatureRepository nomenclatureRepo;

    final String tabcd = "9090";

    @RequestMapping(value = "/endpoint", method = RequestMethod.POST)
    ResponseEntity<?> endpoint(@RequestBody UssdPayloadDTO payload) throws Exception {
        var response = new HashMap<>();

        var session = sessionRepo.findClientByPhoneAndUuid(payload.msisdn, payload.sessionid);

        if (session == null) {
            var res = ussdService.createSessionAndReturnHomeMenu(payload);
            return ResponseEntity.status(HttpStatus.OK).body(res.toString());
        }

        var prevMenu = nomenclatureRepo.findTabcdAndLevelAndRang(tabcd, session.getPos(), payload.message);

        System.out.println(":::::::::::: SESSION PRESENT. GETTING NEXT MENU :::::::::::: ");

        if (prevMenu == null) {
            response.put("message", "Error");
            response.put("command", 1);
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        }

        var nextMenus = nomenclatureRepo.findTabcdAndLevel(tabcd, prevMenu.getLib5());

        System.out.println(":::::::::::: NEXT MENU :::::::::::: " + nextMenus);

        session.setPos(prevMenu.getLib5());
        sessionRepo.save(session);

        response.put("message", ussdService.constructMenuFromNomens(nextMenus));
        response.put("command", 1);

        return ResponseEntity.status(HttpStatus.OK).body(response.toString());
    }
}
