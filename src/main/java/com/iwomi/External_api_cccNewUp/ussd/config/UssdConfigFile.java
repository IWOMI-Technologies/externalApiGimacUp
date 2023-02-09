/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.config;

import com.iwomi.External_api_cccNewUp.ussd.utils.UtilsUssd;

/**
 *
 * @author user
 */
public class UssdConfigFile
{
    
    /*
    This file would read config details from the nomenclature
    
    */
    
    public static String getUserShortCode() {
        return "*098#";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getUser_Short_Code());
    }
    
    public static String getUserCodeSheme() {
        return "15";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getUser_Code_Sheme());
    }
    
    public static String getUserShortCodeService() {
        return "098";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getUser_Short_Code_Service());
    }
    
    public static String getCorrelatorStartUSSDAndStopUSSD() {
        return "6985414";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCorrelatorStartUSSDAndStopUSSD());
    }
    
    public static String getEndPointUssdNOTIFICATION() {
        return "https://154.72.156.106:3000/external_api_ccc/endpoint";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getEndPointUssdNOTIFICATION());
    }
    
    public static String getEndPointIP() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_ENPOINT_IP());
    }
    
    public static String getEndPointPort() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_ENPOINT_PORT());
    }
    
    public static String getSPID() {
        return "2370110013163";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getSPP_SPID());
    }
    
    public static String getSPID_MOMO() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getSPP_SPID_MOMO());
    }
    
    public static String getSPID_USSD() {
        return "2370110013163";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getSPP_SPID_USSD());
    }
    
    public static String getServiceID_MOMO() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getServiceID_MOMO());
    }
    
    public static String getServiceID_USSD() {
        return "";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getServiceID_USSD());
    }
    
    public static String getSPID_SMS() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getSPP_SPID_SMS());
    }
    
    public static String getSPID_Ondemand_Test() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getSPP_SPID_ONDEMAND_TEST());
    }
    
    public static String getServiceID() {
        return "";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SID());
    }
    
    public static String getSPPassword() {
        return "Huawei123";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SP_PASSWORD());
    }
    
    public static String getSPPassword_MOMO() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getPassword_MOMO());
    }
    
    public static String getSPPassword_USSD() {
        return "Huawei123";
        //return UtilsUssd.getPropertiesValue(TemplatesUSSD.getPassword_USSD());
    }
    
    public static String getSPShortCode() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SP_SHORTCODE());
    }
    
    public static String getSPLongCode() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SP_LONGCODE());
    }
    
    public static String getStartNotifyFlag() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_STARTNOTIFYFLAG());
    }
    
    public static String getHttpType() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_HTTP_TYPE());
    }
    
    public static String getSdpIP() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SDP_IP());
    }
    
    public static String getSdpIP_TEST() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SDP_IP_TEST());
    }
    
    public static String getSdpPort() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SDP_PORT());
    }
    
    public static String getSdpConfigNumber() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SDP_Number());
    }
    
    public static String getSdpKeyConfigNumber() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SDP_Key_Number());
    }
    
    public static String getSdpKeyConfigNumberProduction() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SDP_Key_Number_Production());
    }
    
    public static String getSDPKeyProductionSouscription() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getSDP_KeyProduction_Souscription());
    }
    
    public static String getUserHttpType() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getUser_HTTP_TYPE());
    }
    
    public static String getUserIP() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getUser_IP());
    }
    
    public static String getUserPort() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getUser_PORT());
    }
    
    public static String getOA() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SERVICE_OA());
    }
    
    public static String getFA() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getCONFIG_SERVICE_FA());
    }
    
    public static String getSUBSCRIBE_IP() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getSUBSCRIBE_IP());
    }
    
    public static String getSUBSCRIBE_PORT() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getSUBSCRIBE_PORT());
    }
    
    public static String getPRODUCT_ID() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getPRODUCT_ID());
    }
    
    public static String getProduct_ID_Day() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getProduct_ID_Day());
    }
    
    public static String getProduct_ID_Month() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getProduct_ID_Month());
    }
    
    public static String getProduct_ID_Week() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getProduct_ID_Week());
    }
    
    public static String getOPER_CODE() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getOPER_CODE());
    }
    
    public static String getHTTP_TYPE_SUBSCRIBE() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getHTTP_TYPE_SUBSCRIBE());
    }
    
    public static String getHttp_Type_MOMO() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getHttpTypeMomo());
    }
    
    public static String getHttp_Type_USSD() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getHttpTypeUssd());
    }
    
    public static String getStart_Info_Trafic() {
        return UtilsUssd.getPropertiesValue(TemplatesUSSD.getStart_Info_Trafic());
    }
    
    public static String getSendsmsRequestURL() {
        return getHttpType() + "://" + getSdpIP() + ":" + getSdpPort() + "/SendSmsService/services/SendSms";
    }
    
    public static String getSendsmsRequestURL_TEST() {
        return getHttpType() + "://" + getSdpIP_TEST() + ":" + getSdpPort() + "/SendSmsService/services/SendSms";
    }
    
    public static String getOndemandSMSNotificationURL() {
        return getHttpType() + "://" + getSdpIP() + ":" + getSdpPort() + "/SmsNotificationManagerService/services/SmsNotificationManager";
    }
    
    public static String getSubscriptionRequestURL() {
        return getHTTP_TYPE_SUBSCRIBE() + "://" + getSUBSCRIBE_IP() + ":" + getSUBSCRIBE_PORT() + "/SubscribeManageService/services/SubscribeManage";
    }
    
    public static String getEndpointResponseURL() {
        return getHttpType() + "://" + getSdpIP() + ":" + getEndPointPort() + "/response";
    }
    
    public static String getEndpointUserResponseURL() {
        return getUserHttpType() + "://" + getUserIP() + ":" + getUserPort() + "/response";
    }
    
    public static String getStartSmsNotificationURL() {
        return getHttpType() + "://" + getSdpIP() + ":" + getSdpPort() + "/SmsNotificationManagerService/services/SmsNotificationManager";
    }
    
    public static String getStopSmsNotificationURL() {
        return getHttpType() + "://" + getSdpIP() + ":" + getSdpPort() + "SmsNotificationManagerService/services/SmsNotificationManager";
    }
    
    public static String getSendProductionMOMOURL() {
        return getHttp_Type_MOMO() + "://" + getSdpIP() + ":" + getSdpPort() + "/ThirdPartyServiceUMMImpl/UMMServiceService/RequestPayment/v17";
    }
    
    public static String getSendTestMOMOURL() {
        return getHttp_Type_MOMO() + "://" + getSdpIP_TEST() + ":" + getSdpPort() + "/ThirdPartyServiceUMMImpl/UMMServiceService/RequestPayment/v17";
    }
    
    public static String getStartUSSDManagerURL() {
        return "https://41.206.4.162:8443/USSDNotificationManagerService/services/USSDNotificationManager";
    }
    
    public static String getSendUSSDManagerURL() {
        return "https://41.206.4.162:8443/SendUssdService/services/SendUssd";
        //return https://41.206.4.162:8443/SendUssdService/services/SendUssd";
    }
}

