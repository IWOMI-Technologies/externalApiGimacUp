/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.service;

import com.iwomi.External_api_cccNewUp.model.responses;
import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import com.iwomi.External_api_cccNewUp.ussd.config.UssdConfigFile;
import com.iwomi.External_api_cccNewUp.ussd.utils.Factory;
import com.iwomi.External_api_cccNewUp.ussd.utils.UtilsUssd;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author user
 */
public class SendUssdUtils
{
    public static String ValueState;
    
    public static ResponseEntity<?> generateStartSMSUssdRollBack(final String msg, final String phone, final String messageToUser) {
        final Factory factory = new Factory();
        final String timestamp = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
        final String md5 = Factory.generateMD5(UssdConfigFile.getSPID_USSD(), UssdConfigFile.getSPPassword_USSD(), timestamp);
        final String MessageUrl = UssdConfigFile.getSendUSSDManagerURL();
        SendUssdUtils.ValueState = UtilsUssd.getFullNameFromXml(msg, "ns2:senderCB").get(0);
        try {
            final String xmlString = factory.generateFirstSendUSSDStringRollBack(messageToUser, phone, phone, timestamp, md5, phone, msg);
            System.out.println("Start USSD MESSAGE:            " + xmlString);
            final Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssdSession(), -1));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
    }
    
    public static ResponseEntity<?> generateStartSMSUssd(final String msg, final String phone, final String messageToUser) {
        final Factory factory = new Factory();
        final String timestamp = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
        final String md5 = Factory.generateMD5(UssdConfigFile.getSPID_USSD(), UssdConfigFile.getSPPassword_USSD(), timestamp);
        final String MessageUrl = UssdConfigFile.getSendUSSDManagerURL();
        SendUssdUtils.ValueState = UtilsUssd.getFullNameFromXml(msg, "ns2:senderCB").get(0);
        try {
            final String xmlString = factory.generateFirstSendUSSDString(messageToUser, phone, phone, timestamp, md5, phone, msg);
            System.out.println("Start USSD MESSAGE:            " + xmlString);
            final Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssdSession(), -1));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
    }
    
    public static ResponseEntity<?> generateContinueSMSUssd(final String msg, final String phone, final String MessageToSend) {
        final Factory factory = new Factory();
        final String timestamp = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
        final String md5 = Factory.generateMD5(UssdConfigFile.getSPID_USSD(), UssdConfigFile.getSPPassword_USSD(), timestamp);
        final String MessageUrl = UssdConfigFile.getSendUSSDManagerURL();
        try {
            final String xmlString = factory.generateContinueSendUSSDString(MessageToSend, phone, phone, timestamp, md5, phone, msg);
            System.out.println("Start USSD MESSAGE:             " + xmlString);
            final Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssdSession(), -1));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
    }
    
    public static ResponseEntity<?> generateContinueUssdResendUserPassword(final String msg, final String phone, final String MessageToSend) {
        final Factory factory = new Factory();
        final String timestamp = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
        final String md5 = Factory.generateMD5(UssdConfigFile.getSPID_USSD(), UssdConfigFile.getSPPassword_USSD(), timestamp);
        final String MessageUrl = UssdConfigFile.getSendUSSDManagerURL();
        try {
            final String xmlString = factory.generateContinueSendUSSDString(MessageToSend, phone, phone, timestamp, md5, phone, msg);
            System.out.println("Please Give your Password:             " + xmlString);
            final Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssdSession(), -1));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
    }
    
    public static ResponseEntity<?> generateContinueUssdAfterPasswordOK(final String msg, final String phone, final String MessageToSend) {
        final Factory factory = new Factory();
        final String timestamp = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
        final String md5 = Factory.generateMD5(UssdConfigFile.getSPID_USSD(), UssdConfigFile.getSPPassword_USSD(), timestamp);
        final String MessageUrl = UssdConfigFile.getSendUSSDManagerURL();
        try {
            final String xmlString = factory.generateContinueSendUSSDString(MessageToSend, phone, phone, timestamp, md5, phone, msg);
            System.out.println("Please Give your Password:             " + xmlString);
            final Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssdSession(), -1));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
    }
    
    public static ResponseEntity<?> generateContinueUssdCheckUserInput(final String msg, final String phone, final String MessageToSend) {
        final Factory factory = new Factory();
        final String timestamp = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
        final String md5 = Factory.generateMD5(UssdConfigFile.getSPID_USSD(), UssdConfigFile.getSPPassword_USSD(), timestamp);
        final String MessageUrl = UssdConfigFile.getSendUSSDManagerURL();
        try {
            final String xmlString = factory.generateContinueSendUSSDString(MessageToSend, phone, phone, timestamp, md5, phone, msg);
            System.out.println("we require input from this:             " + xmlString);
            final Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssdSession(), -1));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
    }
    
    public static ResponseEntity<?> generateLastMsgNotReplyByUserUSSD(final String msg, final String phone, final String MessageToSend) {
        final Factory factory = new Factory();
        final String timestamp = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
        final String md5 = Factory.generateMD5(UssdConfigFile.getSPID_USSD(), UssdConfigFile.getSPPassword_USSD(), timestamp);
        final String MessageUrl = UssdConfigFile.getSendUSSDManagerURL();
        try {
            final String xmlString = factory.generateLastMsgNotReplyByUserUSSD(MessageToSend, phone, phone, timestamp, md5, phone, msg);
            System.out.println("Start USSD MESSAGE:             " + xmlString);
            final Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssdSession(), -1));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
    }
    
    public static ResponseEntity<?> generateErrorUserNotEnterINTEGERValue(final String msg, final String phone, final String MessageToSend) {
        final Factory factory = new Factory();
        final String timestamp = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
        final String md5 = Factory.generateMD5(UssdConfigFile.getSPID_USSD(), UssdConfigFile.getSPPassword_USSD(), timestamp);
        final String MessageUrl = UssdConfigFile.getSendUSSDManagerURL();
        try {
            final String xmlString = factory.generateNotUserSendUSSDString(MessageToSend, phone, phone, timestamp, md5, phone, msg);
            System.out.println("Start USSD MESSAGE:             " + xmlString);
            final Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssdSession(), -1));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
    }
    
    public static ResponseEntity<?> generateNotUserSMSUssd(final String msg, final String phone, final String MessageToSend) {
        final Factory factory = new Factory();
        final String timestamp = UtilsUssd.generateTimeStamp("yyyyMMddhhmmss");
        final String md5 = Factory.generateMD5(UssdConfigFile.getSPID_USSD(), UssdConfigFile.getSPPassword_USSD(), timestamp);
        final String MessageUrl = UssdConfigFile.getSendUSSDManagerURL();
        try {
            final String xmlString = factory.generateLastMsgNotReplyByUserUSSD(MessageToSend, phone, phone, timestamp, md5, phone, msg);
            System.out.println("Start USSD MESSAGE:             " + xmlString);
            final Object response = new UtilsUssd().doHttpPost(MessageUrl, xmlString);
            if (Objects.isNull(response)) {
                return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailStartUssdSession(), -1));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
    }
    
    static {
        SendUssdUtils.ValueState = "";
    }
}

