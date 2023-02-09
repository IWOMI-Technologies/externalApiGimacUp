/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.utils;

import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import java.util.List;
import org.springframework.context.MessageSource;

/**
 *
 * @author user
 */
public class UtilsUssd2
{
    
    
    public static String viewUserChoiceListOnUssd(final List<String> listchoice, final String langs, final MessageSource messageSource) {
        String resultString = "";
        int i = 1;
        if (listchoice.size() > 0) {
            resultString = UtilsUssd.getMessage("selectchoice", langs, messageSource);
            for (final String string : listchoice) {
                resultString = resultString + "\n" + i + TemplatesUSSD.getSeparatorussdlist() + TemplatesUSSD.getEspaceonussdstring() + string;
                ++i;
            }
            return resultString;
        }
        return null;
    }
    
    public static String selectPeriodForJourney(final List<String> listchoice, final String langs, final MessageSource messageSource) {
        String resultString = "";
        int i = 1;
        if (listchoice.size() > 0) {
            resultString = UtilsUssd.getMessage("selectchoice", langs, messageSource);
            for (final String string : listchoice) {
                resultString = resultString + "\n" + i + TemplatesUSSD.getSeparatorussdlist() + TemplatesUSSD.getEspaceonussdstring() + string;
                ++i;
            }
            return resultString;
        }
        return null;
    }
}

