package com.iwomi.External_api_cccNewUp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WsocketCont {

        @MessageMapping("/chat.sendMessage")
        @SendTo("/topic")
        public WsocketCont sendMessage(@Payload WsocketCont wsocketCont) {
        return wsocketCont;
    }

        @MessageMapping("/chat.newUser")
        @SendTo("/topic")
        public WsocketCont newUser(@Payload WsocketCont wsocketCont,
            SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", wsocketCont.getClass());
        return wsocketCont;
    }

    }
