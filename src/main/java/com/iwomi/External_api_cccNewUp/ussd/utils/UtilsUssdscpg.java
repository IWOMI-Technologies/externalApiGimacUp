/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.utils;

import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author user
 */
public class UtilsUssdscpg
{
    public static String factorisePosition(final String[] position) {
        final int length = position.length;
        int k = 0;
        String result = "";
        if (length >= TemplatesUSSD.getMinimumpositiontorollback()) {
            for (k = 0; k < length - 2; ++k) {
                if (k == 0) {
                    result = position[k];
                }
                else {
                    result = result + TemplatesUSSD.getBaseSeparateurChoice() + position[k];
                }
            }
            return result;
        }
        return null;
    }
    
    public static String FormatDate(final String date) {
        final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        final SimpleDateFormat sdf2 = new SimpleDateFormat(TemplatesUSSD.getFormatdatetrajetussd());
        try {
            return sdf2.format(sdf.parse(date));
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Date reelFormatDate(final String date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(TemplatesUSSD.getReelformatdate());
        try {
            return sdf.parse(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String TodayDate(final int date) {
        if (date != 1 && date != 2) {
            return null;
        }
        final SimpleDateFormat sdf2 = new SimpleDateFormat(TemplatesUSSD.getFormatdatetrajetussd());
        if (date == 1) {
            return sdf2.format(new Date());
        }
        final Calendar dateB = new GregorianCalendar();
        dateB.setTime(new Date());
        dateB.add(5, -1);
        dateB.getTime();
        return sdf2.format(dateB.getTime());
    }
    
    public static boolean formatDateValidation(final String date) {
        if (date.length() == TemplatesUSSD.getLengtdate()) {
            final String dayString = date.substring(0, 2);
            final String monthString = date.substring(2, 4);
            final String year = date.substring(4, 6);
            final int dayint = Integer.valueOf(dayString);
            final int monthint = Integer.valueOf(monthString);
            final int yearint = Integer.valueOf(year);
            return dayint <= 31 && dayint != 0 && !(monthint == 0 | monthint > 12) && yearint != 0 && yearint >= 19;
        }
        return false;
    }
    public static boolean isInt(final String s) {
        try {
            final int i = Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException er) {
            return false;
        }
    }
    
    
}

