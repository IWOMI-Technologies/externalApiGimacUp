/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.utils;
import com.iwomi.External_api_cccNewUp.ussd.config.UssdConfigFile;
import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author user
 */
public class Factory
{
    public String getEncodage() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    }
    
    public static String getEncodageMomo() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    }
    
    public static String getEncodageSMS() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    }
    
    
    
    public String generateContinueSendUSSDString(final String messageUSSD, final String OA, final String FA, final String timestamp, final String md5, final String phone, final String msg) throws NoSuchAlgorithmException {
        String sendSmsString = null;
        try {
            sendSmsString = getEncodageSMS() + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:loc=\"http://www.csapi.org/schema/parlayx/ussd/send/v1_0/local\"> \n <soapenv:Header> \n <tns:RequestSOAPHeader xmlns:tns=\"http://www.huawei.com.cn/schema/common/v2_1\"> \n <tns:spId>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSpid()).get(0) + "</tns:spId> \n <tns:spPassword>" + md5 + "</tns:spPassword> \n <tns:serviceId/> \n <tns:timeStamp>" + timestamp + "</tns:timeStamp> \n <tns:OA>" + OA + "</tns:OA>\n <tns:FA>" + FA + "</tns:FA>\n </tns:RequestSOAPHeader> \n </soapenv:Header>\n <soapenv:Body> \n <loc:sendUssd> \n <loc:msgType>1</loc:msgType> \n <loc:senderCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagReceiverCb()).get(0) + "</loc:senderCB> \n <loc:receiveCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSenderCb()).get(0) + "</loc:receiveCB> \n <loc:ussdOpType>1</loc:ussdOpType> \n <loc:msIsdn>" + phone + "</loc:msIsdn>\n <loc:serviceCode>" + UssdConfigFile.getUserShortCodeService() + "</loc:serviceCode> \n <loc:codeScheme>" + UssdConfigFile.getUserCodeSheme() + "</loc:codeScheme>\n <loc:ussdString>" + messageUSSD + "</loc:ussdString>\n<loc:endPoint/></loc:sendUssd> \n</soapenv:Body> \n</soapenv:Envelope>";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sendSmsString;
    }
    
    public String generateLastMsgNotReplyByUserUSSD(final String messageUSSD, final String OA, final String FA, final String timestamp, final String md5, final String phone, final String msg) throws NoSuchAlgorithmException {
        String sendSmsString = null;
        try {
            sendSmsString = getEncodageSMS() + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:loc=\"http://www.csapi.org/schema/parlayx/ussd/send/v1_0/local\"> \n <soapenv:Header> \n <tns:RequestSOAPHeader xmlns:tns=\"http://www.huawei.com.cn/schema/common/v2_1\"> \n <tns:spId>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSpid()).get(0) + "</tns:spId> \n <tns:spPassword>" + md5 + "</tns:spPassword> \n <tns:serviceId/> \n <tns:timeStamp>" + timestamp + "</tns:timeStamp> \n <tns:OA>" + OA + "</tns:OA>\n <tns:FA>" + FA + "</tns:FA>\n </tns:RequestSOAPHeader> \n </soapenv:Header>\n <soapenv:Body> \n <loc:sendUssd> \n <loc:msgType>2</loc:msgType> \n <loc:senderCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagReceiverCb()).get(0) + "</loc:senderCB> \n <loc:receiveCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSenderCb()).get(0) + "</loc:receiveCB> \n <loc:ussdOpType>1</loc:ussdOpType> \n <loc:msIsdn>" + phone + "</loc:msIsdn>\n <loc:serviceCode>" + UssdConfigFile.getUserShortCodeService() + "</loc:serviceCode> \n <loc:codeScheme>" + UssdConfigFile.getUserCodeSheme() + "</loc:codeScheme>\n <loc:ussdString>" + messageUSSD + "</loc:ussdString>\n<loc:endPoint/></loc:sendUssd> \n</soapenv:Body> \n</soapenv:Envelope>";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sendSmsString;
    }
    
    public String generateNotUserSendUSSDString(final String messageUSSD, final String OA, final String FA, final String timestamp, final String md5, final String phone, final String msg) throws NoSuchAlgorithmException {
        String sendSmsString = null;
        try {
            sendSmsString = getEncodageSMS() + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:loc=\"http://www.csapi.org/schema/parlayx/ussd/send/v1_0/local\"> \n <soapenv:Header> \n <tns:RequestSOAPHeader xmlns:tns=\"http://www.huawei.com.cn/schema/common/v2_1\"> \n <tns:spId>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSpid()).get(0) + "</tns:spId> \n <tns:spPassword>" + md5 + "</tns:spPassword> \n <tns:serviceId/> \n <tns:timeStamp>" + timestamp + "</tns:timeStamp> \n <tns:OA>" + OA + "</tns:OA>\n <tns:FA>" + FA + "</tns:FA>\n </tns:RequestSOAPHeader> \n </soapenv:Header>\n <soapenv:Body> \n <loc:sendUssd> \n <loc:msgType>2</loc:msgType> \n <loc:senderCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagReceiverCb()).get(0) + "</loc:senderCB> \n <loc:receiveCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSenderCb()).get(0) + "</loc:receiveCB> \n <loc:ussdOpType>1</loc:ussdOpType> \n <loc:msIsdn>" + phone + "</loc:msIsdn>\n <loc:serviceCode>" + UssdConfigFile.getUserShortCodeService() + "</loc:serviceCode> \n <loc:codeScheme>" + UssdConfigFile.getUserCodeSheme() + "</loc:codeScheme>\n <loc:ussdString>" + messageUSSD + "</loc:ussdString>\n<loc:endPoint/></loc:sendUssd> \n</soapenv:Body> \n</soapenv:Envelope>";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sendSmsString;
    }
    
    public String generateFirstSendUSSDStringRollBack(final String messageUSSD, final String OA, final String FA, final String timestamp, final String md5, final String phone, final String msg) throws NoSuchAlgorithmException {
        String sendSmsString = null;
        try {
            sendSmsString = getEncodageSMS() + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:loc=\"http://www.csapi.org/schema/parlayx/ussd/send/v1_0/local\"> \n <soapenv:Header> \n <tns:RequestSOAPHeader xmlns:tns=\"http://www.huawei.com.cn/schema/common/v2_1\"> \n <tns:spId>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSpid()).get(0) + "</tns:spId> \n <tns:spPassword>" + md5 + "</tns:spPassword> \n <tns:timeStamp>" + timestamp + "</tns:timeStamp> \n <tns:OA>" + OA + "</tns:OA>\n <tns:FA>" + FA + "</tns:FA>\n </tns:RequestSOAPHeader> \n </soapenv:Header>\n <soapenv:Body> \n <loc:sendUssd> \n <loc:msgType>1</loc:msgType> \n <loc:senderCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagReceiverCb()).get(0) + "</loc:senderCB> \n <loc:receiveCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSenderCb()).get(0) + "</loc:receiveCB> \n <loc:ussdOpType>1</loc:ussdOpType> \n <loc:msIsdn>" + phone + "</loc:msIsdn>\n <loc:serviceCode>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagServiceCode()).get(0) + "</loc:serviceCode> \n <loc:codeScheme>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagCodescheme()).get(0) + "</loc:codeScheme>\n <loc:ussdString>" + messageUSSD + "</loc:ussdString>\n </loc:sendUssd> \n </soapenv:Body> \n </soapenv:Envelope>";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sendSmsString;
    }
    
    public String generateFirstSendUSSDString(final String messageUSSD, final String OA, final String FA, final String timestamp, final String md5, final String phone, final String msg) throws NoSuchAlgorithmException {
        String sendSmsString = null;
        try {
            sendSmsString = getEncodageSMS() + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:loc=\"http://www.csapi.org/schema/parlayx/ussd/send/v1_0/local\"> \n <soapenv:Header> \n <tns:RequestSOAPHeader xmlns:tns=\"http://www.huawei.com.cn/schema/common/v2_1\"> \n <tns:spId>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSpid()).get(0) + "</tns:spId> \n <tns:spPassword>" + md5 + "</tns:spPassword> \n <tns:timeStamp>" + timestamp + "</tns:timeStamp> \n <tns:OA>" + OA + "</tns:OA>\n <tns:FA>" + FA + "</tns:FA>\n </tns:RequestSOAPHeader> \n </soapenv:Header>\n <soapenv:Body> \n <loc:sendUssd> \n <loc:msgType>" + TemplatesUSSD.getBeginUssdMessageTypeString() + "</loc:msgType> \n <loc:senderCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagReceiverCb()).get(0) + "</loc:senderCB> \n <loc:receiveCB>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagSenderCb()).get(0) + "</loc:receiveCB> \n <loc:ussdOpType>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagUssdOptype()).get(0) + "</loc:ussdOpType> \n <loc:msIsdn>" + phone + "</loc:msIsdn>\n <loc:serviceCode>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagServiceCode()).get(0) + "</loc:serviceCode> \n <loc:codeScheme>" + UtilsUssd.getFullNameFromXml(msg, TemplatesUSSD.getTagCodescheme()).get(0) + "</loc:codeScheme>\n <loc:ussdString>" + messageUSSD + "</loc:ussdString>\n </loc:sendUssd> \n </soapenv:Body> \n </soapenv:Envelope>";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sendSmsString;
    }
    
    public static String generateMD5(final String spid, final String password, final String timestamp) {
        return UtilsUssd.generateMD5(spid, password, timestamp);
    }
    
    public String generateStartUSSDNotification(final String spid, final String timestamp, final String md5, final String serviceid, final String endpoint, final String correlator, final String ussdCode) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:loc=\"http://www.csapi.org/schema/osg/ussd/notification_manager/v1_0/local\">\n<soapenv:Header> \n<tns:RequestSOAPHeader xmlns:tns=\"http://www.huawei.com.cn/schema/common/v2_1\"> \n<tns:spId>" + spid + "</tns:spId> \n<tns:spPassword>" + md5 + "</tns:spPassword>\n <tns:serviceId>" + serviceid + "</tns:serviceId> \n <tns:timeStamp>" + timestamp + "</tns:timeStamp> \n </tns:RequestSOAPHeader> \n </soapenv:Header> \n <soapenv:Body> \n <loc:startUSSDNotification> \n <loc:reference> \n <endpoint>" + endpoint + "</endpoint> \n <interfaceName>notifyUssdReception</interfaceName>\n <correlator>" + correlator + "</correlator> \n </loc:reference> \n <loc:ussdServiceActivationNumber>" + ussdCode + "</loc:ussdServiceActivationNumber>\n </loc:startUSSDNotification> \n </soapenv:Body>\n </soapenv:Envelope>";
    }
    
    
    public String generateSendSmsStringCorrelator(final String spid, final String password, final String timestamp, final String phone, final String msg, final String PhoneSDP, final String service_id, final String correlator) throws NoSuchAlgorithmException {
        final String md5 = UtilsUssd.generateMD5(spid, password, timestamp);
        final String sendSmsString = getEncodageSMS() + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v2=\"http://www.huawei.com.cn/schema/common/v2_1\" xmlns:loc=\"http://www.csapi.org/schema/parlayx/sms/send/v2_2/local\"> \n    <soapenv:Header> \n        <v2:RequestSOAPHeader> \n            <v2:spId>" + spid + "</v2:spId> \n            <v2:spPassword>" + md5 + "</v2:spPassword>\n            <v2:serviceId>" + service_id + "</v2:serviceId> \n            <v2:timeStamp>" + timestamp + "</v2:timeStamp> \n            <v2:OA>" + phone + "</v2:OA> \n            <v2:FA>" + phone + "</v2:FA>\n        </v2:RequestSOAPHeader> \n    </soapenv:Header> \n    <soapenv:Body> \n        <loc:sendSms> \n            <loc:addresses>tel:" + phone + "</loc:addresses> \n            <loc:senderName>" + PhoneSDP + "</loc:senderName> \n            <loc:message>" + msg + "</loc:message> \n            <loc:receiptRequest> \n                <endpoint>https://5.135.199.81:8443/infotrafic/webresources/services/smsUser</endpoint> \n                <interfaceName>SmsNotification</interfaceName> \n                <correlator>" + correlator + "</correlator> \n            </loc:receiptRequest> \n        </loc:sendSms> \n    </soapenv:Body> \n</soapenv:Envelope>";
        return sendSmsString;
    }
}

