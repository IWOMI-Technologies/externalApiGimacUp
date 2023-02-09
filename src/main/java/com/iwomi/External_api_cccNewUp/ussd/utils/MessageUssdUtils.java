/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.utils;

import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import org.springframework.context.MessageSource;

/**
 *
 * @author user
 */
public class MessageUssdUtils
{
    public static String wrongUserSelected(final String phone, final String langs, final MessageSource messageSource) {
        //user did not enter an integrer in the response
        if (langs.equalsIgnoreCase("fr")){
            return "Valeur incorrecte";
        }
        else{
            return "Please Enter a correct Integer";
        }
    }
    
    public static String wrongUserSelectedToHighNumber(final String phone, final String langs, final MessageSource messageSource) {
        return UtilsUssd.getMessage("pleaseintegerlower", langs, messageSource);
    }
    
    public static String wrongValueSelect(final String phone, final String langs, final MessageSource messageSource) {
        return UtilsUssd.getMessage("pleaseseinvalidevalue", langs, messageSource);
    }
    
    public static String ResendPIN(final String phone, final String langs, final MessageSource messageSource) {
        return UtilsUssd.getMessage("recheckpassword", langs, messageSource);
    }
    
    public static String NotUSerMessage(final String phone) {
        return TemplatesUSSD.getErrorNotUserString(phone);
    }
}

