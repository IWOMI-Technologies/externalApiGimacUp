package com.iwomi.External_api_cccNewUp.controller;

import com.iwomi.External_api_cccNewUp.Core.constants.Menu;
import com.iwomi.External_api_cccNewUp.Dto.UssdPayloadDTO;
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

    @RequestMapping(value = "/endpoint", method = RequestMethod.POST)
    ResponseEntity<?> endpoint(@RequestBody UssdPayloadDTO payload) {
        try {
            log.info(":::::::::::: Payload :::::::::::: " + payload);

            var session = sessionService.getSessionByPhoneAndSsid(payload.msisdn, payload.sessionid);

            if (session == null) {
                log.info(":::::::::::: SESSION NOT PRESENT. CREATING NEW SESSION :::::::::::: ");
                var res = ussdService.createSessionAndReturnHomeMenu(payload);
                return ResponseEntity.status(HttpStatus.OK).body(res.toString());
            }

            log.info(":::::::::::: SESSION PRESENT :::::::::::: " + session);
            log.info(":::::::::::: CURRENT LEVEL :::::::::::: " + session.getPos());

            if (!session.getPos().equalsIgnoreCase(Menu.StartLevel)) {
                if (payload.message.equalsIgnoreCase(Menu.HomeMenu)) {
                    log.info(":::::::::::: User Chooses Home Menu :::::::::::: ");

                    session.setPos(Menu.StartLevel);
                    sessionService.saveSession(session);

                    var res = ussdService.getMenuItems(Menu.StartLevel);
                    return ResponseEntity.status(HttpStatus.OK).body(res.toString());
                }

                if (payload.message.equalsIgnoreCase(Menu.PrevMenu)) {
                    log.info(":::::::::::: User Chooses prev Menu :::::::::::: ");

                    var res = ussdService.goPrevMenu(session.getPos(), session);

                    return ResponseEntity.status(HttpStatus.OK).body(res.toString());
                }
            }

            var res = ussdService.goToUserChooseMenu(session.getPos(), payload.message, session);
            return ResponseEntity.status(HttpStatus.OK).body(res.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body(ussdService.returnNoMenuFound(e.getMessage()).toString());
        }
    }
}
