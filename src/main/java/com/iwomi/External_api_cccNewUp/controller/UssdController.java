package com.iwomi.External_api_cccNewUp.controller;

import com.iwomi.External_api_cccNewUp.Core.constants.Menu;
import com.iwomi.External_api_cccNewUp.Dto.UssdPayloadDTO;
import com.iwomi.External_api_cccNewUp.repo.NomenclatureRepository;
import com.iwomi.External_api_cccNewUp.service.SessionService;
import com.iwomi.External_api_cccNewUp.service.UssdService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiPrefix}")
@Component
@RequiredArgsConstructor

public class UssdController {
    private static final Log log = LogFactory.getLog(UssdController.class);
    @Autowired
    UssdService ussdService;

    @Autowired
    final SessionService sessionService;
    @Autowired
    NomenclatureRepository nomenclatureRepo;

    @RequestMapping(value = "/endpoint", method = RequestMethod.POST)
    ResponseEntity<?> endpoint(@RequestBody UssdPayloadDTO payload) throws Exception {
        log.info(":::::::::::: Payload :::::::::::: " + payload);

        var session = sessionService.getSessionByPhoneAndSsid(payload.msisdn, payload.sessionid);

        log.info(":::::::::::: SESSION NOT PRESENT. CREATING NEW SESSION :::::::::::: ");
        if (session == null) {
            var res = ussdService.createSessionAndReturnHomeMenu(payload);
            return ResponseEntity.status(HttpStatus.OK).body(res.toString());
        }

        log.info(":::::::::::: SESSION PRESENT. User Picked Bottom MENU :::::::::::: ");
        if (!session.getPos().equalsIgnoreCase(Menu.StartLevel)) {
            if (payload.message.equalsIgnoreCase(Menu.HomeMenu)) {
                log.info(":::::::::::: Using Home Menu :::::::::::: ");

                session.setPos(Menu.StartLevel);
                sessionService.saveSession(session);

                var res = ussdService.getMenuItems(Menu.StartLevel);
                return ResponseEntity.status(HttpStatus.OK).body(res.toString());
            }

            if (payload.message.equalsIgnoreCase(Menu.PrevMenu)) {
                var pair = ussdService.goPrevMenu(session.getPos());

                session.setPos(pair.get1st());
                sessionService.saveSession(session);

                return ResponseEntity.status(HttpStatus.OK).body(pair.get2nd().toString());
            }
        }

        log.info(":::::::::::: SESSION PRESENT. GETTING NEXT MENU :::::::::::: ");
        var pair = ussdService.goToUserChooseMenu(session.getPos(), payload.message);

        session.setPos(pair.get1st());
        sessionService.saveSession(session);

        return ResponseEntity.status(HttpStatus.OK).body(pair.get2nd().toString());
    }
}
