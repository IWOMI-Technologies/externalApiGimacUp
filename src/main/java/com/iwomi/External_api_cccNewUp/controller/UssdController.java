package com.iwomi.External_api_cccNewUp.controller;

import com.google.gson.Gson;
import com.iwomi.External_api_cccNewUp.Dto.TransferInfo;
import com.iwomi.External_api_cccNewUp.Dto.UssdPayloadDTO;
import com.iwomi.External_api_cccNewUp.service.ApiClient;
import com.iwomi.External_api_cccNewUp.service.SessionService;
import com.iwomi.External_api_cccNewUp.service.UssdService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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
    final ApiClient apiClient;

    @PostMapping("/test")
    ResponseEntity<?> test(@RequestBody UssdPayloadDTO payload) {
        var body = new HashMap<String, Object>();

        final Gson gson = new Gson();
        var amount = "200";
        var wallet = "237677530318";

        body.put("wallet", wallet);
        body.put("amount", amount);

        var result = apiClient.post("/transferDetail", new HashMap<String, Object>(), body);

        System.out.println("::::::::: RESULT  :::::::::::: " + result);

        if (result.getStatus() == 200) {
            var info = gson.fromJson(result.getBody().toString(), TransferInfo.class);
            System.out.println("::::::::: info  :::::::::::: " + info);

            var str = "";

            if (info.can) {
                str = "You are about to transfer " + amount + " to " + wallet + "-" + info.fullName + ". Fees: " + info.fees;
            } else {
                str = info.reason + " to transfer " + amount + " to " + wallet + "-" + info.fullName + ". Fees: " + info.fees;
            }

            System.out.println("::: STR :::" + str);
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    @PostMapping(value = "/endpoint")
    ResponseEntity<?> endpoint(@RequestBody UssdPayloadDTO payload) {
        try {
            log.info(":::::::::::: Payload :::::::::::: " + payload);

            var session = sessionService.getSessionByPhoneAndSsid(payload.msisdn, payload.sessionid);

            if (session == null) {
                var res = ussdService.createSessionAndReturnHomeMenu(payload);
                return ResponseEntity.status(HttpStatus.OK).body(res.toString());
            }

            var res = ussdService.goToUserChooseMenu(session.getPos(), payload.message, session);

            return ResponseEntity.status(HttpStatus.OK).body(res.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body(ussdService.returnNoMenuFound(e.getMessage()).toString());
        }
    }
}
