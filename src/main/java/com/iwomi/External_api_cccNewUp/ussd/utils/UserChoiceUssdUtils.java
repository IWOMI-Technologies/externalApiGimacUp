/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.utils;

import com.iwomi.External_api_cccNewUp.model.UserSession;
import com.iwomi.External_api_cccNewUp.repository.UserSessionRepo;
import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import com.iwomi.External_api_cccNewUp.ussd.service.SendUssdUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author user
 */
@Component
public class UserChoiceUssdUtils
{
    
    public static void generatePossiblyMessageToUserpgfour(final String MTNNotifications, final String phone, final String vehicle, final Integer userchoiceInteger, final Integer oldUserSelectionInteger, final UserSession user, final UserSessionRepo userRepo,MessageSource messageSource) {
        user.setPos(user.getPos()+ TemplatesUSSD.getBaseSeparateurChoice() + userchoiceInteger);
        userRepo.saveAndFlush(user);
        switch (oldUserSelectionInteger) {
        case 1: {
            try {
            	SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MTNNotifications, phone, "MessageToSend");
            }
            catch (Exception ex) {
                SendUssdUtils.generateLastMsgNotReplyByUserUSSD(MTNNotifications, phone, UtilsUssd.getMessage("systemerror", user.getLang(), messageSource));
                ex.printStackTrace();
            }
            break;
        }
        }
        
    }
    
    
}
