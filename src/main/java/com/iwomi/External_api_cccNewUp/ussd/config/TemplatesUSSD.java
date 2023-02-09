/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.config;

/**
 *
 * @author user
 */
public class TemplatesUSSD
{
    private static  Integer LENGTHBCRYT;
    private static  Integer LENGTDATE;
    private static  String BASE_USER_POSITION_ON_STRING_USSD;
    public static String getOut2inuse() {
        return "DOUT2:Already";
    }
    
    public static String getOut1queued() {
        return "DOUT1:QUEUED";
    }
    
    public static String getOut2queued() {
        return "DOUT2:QUEUED";
    }
    
    
    public static String getOut11() {
        return "DOUT1:1";
    }
    
    public static String getOut10() {
        return "DOUT1:0";
    }
    
    public static String getOut21() {
        return "DOUT2:1";
    }
    
    public static String getOut20() {
        return "DOUT2:0";
    }
    
    public static String getTagMessageText() {
        return "message";
    }
    
    public static String getTagSenderaddressText() {
        return "senderAddress";
    }
    
    public static String getDesactivategenericservice() {
        return "desactivategenericservice";
    }
    
    public static String getActivategenericservice() {
        return "activategenericservice";
    }
    
    public static String getSmsansemail() {
        return "smsansemail";
    }
    
    public static String getSms() {
        return "sms";
    }
    
    public static String getEmail() {
        return "email";
    }
    
    public static int getActivatedesireservice() {
        return 1;
    }
    
    public static int getDesactivatedesireservice() {
        return 0;
    }
    
    public static int getPositionupdategeofencechoice() {
        return 8;
    }
    
    public static String getActivateservice() {
        return "activateservice";
    }
    
    public static String getListgeofence() {
        return "listgeofence";
    }
    
    public static String getActivatesmsemail() {
        return "activatesmsemail";
    }
    
    public static String getActivatemail() {
        return "activatemail";
    }
    
    public static String getActivatesms() {
        return "activatesms";
    }
    
    public static String getDesactivatemail() {
        return "desactivatemail";
    }
    
    public static String getDesactivatesms() {
        return "desactivatesms";
    }
    
    public static String getDesactivatesmsemail() {
        return "desactivatesmsemail";
    }
    
    public static String getGeofenceTag() {
        return "_";
    }
    
    public static int getMinimumpositiontorollback() {
        return 6;
    }
    
    public static String getRetourmainmenu() {
        return "99";
    }
    
    public static String getSelectedzero() {
        return "0";
    }
    
    public static int getPositionprofiluserselected() {
        return 5;
    }
    
    public static int getPositionupdateprofil() {
        return 7;
    }
    
    public static int getPositionprofil() {
        return 6;
    }
    
    public static int getMaxProfilUserChange() {
        return 2;
    }
    
    public static String getRemovebeginningsubscriptionperuser3or6() {
        return "Part_";
    }
    
    public static String getRemovebeginningsubscriptionperuser() {
        return "Part_1_";
    }
    
    public static String getObliquebar() {
        return "/";
    }
    
    public static String getFrancFa() {
        return " F";
    }
    
    public static String getReelemptystring() {
        return "";
    }
    
    public static String getRemovebeginningsubscription() {
        return "Part_";
    }
    
    public static String getEmptystring() {
        return " ";
    }
    
    
    public static String getSeparateurtrajet() {
        return " ----- ";
    }
    
    public static String getReelformatdate() {
        return "yyyy-MM-dd HH:mm:ss";
    }
    
    public static String getFormatdatetrajetussd() {
        return "yyyy-MM-dd";
    }
    
    public static String getFormatdatebegin() {
        return "00:00:00";
    }
    
    public static String getFormatdateend() {
        return "23:59:59";
    }
    
    public static String getWrongdateformat() {
        return "Wrongdateformat";
    }
    
    public static Integer getLengtdate() {
        return TemplatesUSSD.LENGTDATE;
    }
    
    public static int getPositionselectedtrajet() {
        return 6;
    }
    
    public static String getLinereturnString() {
        return "\n";
    }
    
    public static int getUserselectedpositionvehicle() {
        return 4;
    }
    
    public static int getPositionselectedvehicle() {
        return 5;
    }
    
    public static String getBaseSeparateurChoiceSplittingString() {
        return "\\*";
    }
    
    public static String getEspaceonussdstring() {
        return " ";
    }
    
    public static String getSeparatorussdlist() {
        return ":";
    }
    
    public static Integer getLengthbcryt() {
        return TemplatesUSSD.LENGTHBCRYT;
    }
    
    public static String getTagServiceId() {
        return "ns1:serviceId";
    }
    
    public static String getTagServiceCode() {
        return "ns2:serviceCode";
    }
    
    public static String getTagCodescheme() {
        return "ns2:codeScheme";
    }
    
    public static String getTagUssdOptype() {
        return "ns2:ussdOpType";
    }
    
    public static String getTagSpid() {
        return "ns1:spId";
    }
    
    public static String getTagReceiverCb() {
        return "ns2:receiveCB";
    }
    
    public static String getTagSenderCb() {
        return "ns2:senderCB";
    }
    
    public static String getTagMsdnText() {
        return "ns2:msIsdn";
    }
    
    public static String getTagTexteString() {
        return "ns2:ussdString";
    }
    
    public static String getBaseSeparateurChoice() {
        return "*";
    }
    
    public static String getFailStartSms() {
        return "fail start sms";
    }
    
    public static String getBaseUserPositionOnStringUssdWaitingResponseCode() {
        return "WaPs";
    }
    
    public static String getFailStartUssdSession() {
        return "fail start ussd session";
    }
    
    public static String getFailStartUssd() {
        return "fail start ussd";
    }
    
    public static String getErrorNotUserString( String phone) {
        return "Your number "+phone+" is not subscribed to this service, kindly approach your CCC PLC Branch to get subscribe";
    }
    
    public static String getSendUssdAbortString() {
        return "sendUssdAbort";
    }
    
    public static String getNotificationabort() {
        return "notifyUssdAbort";
    }
    
    public static String getBeginUssdMessageTypeString() {
        return "1";
    }
    
    public static String getEmptyString() {
        return " ";
    }
    
    public static String getBaseUserPositionOnStringUssd() {
        return TemplatesUSSD.BASE_USER_POSITION_ON_STRING_USSD;
    }
    
    public static String getAttchUserPositionOnStringUssd() {
        return getBaseSeparateurChoice() + "atch";
    }
    
    public static String getBaseLocaleString() {
        return "fr";
    }
    
    public static String getEndUssdMessageTypeString() {
        return "2";
    }
    
    public static String getSmsSuccessSubscriptionString() {
        return "Subsription R\u00e9ussie pour vos v\u00e9hicules";
    }
    
    public static String getHttpTypeMomo() {
        return "Http_Type_MOMO";
    }
    
    public static String getHttpTypeUssd() {
        return "Http_Type_USSD";
    }
    
    public static String getFailSubscriptionString() {
        return "fail subscription";
    }
    
    public static String getSuccessString() {
        return "success";
    }
    
    public static String getSubscriptionExistsString() {
        return "subscription exists";
    }
    
    public static String getUser_Short_Code() {
        return "User_Short_Code";
    }
    
    public static String getUser_Short_Code_Service() {
        return "User_Short_Code_Service";
    }
    
    public static String getUser_Code_Sheme() {
        return "User_Code_Sheme";
    }
    
    public static String getCorrelatorStartUSSDAndStopUSSD() {
        return "CorrelatorStartUSSDAndStopUSSD";
    }
    
    public static String getEndPointUssdNOTIFICATION() {
        return "End_POINT_USSD_NOTIFICATION";
    }
    
    public static String getCONFIG_ENPOINT_IP() {
        return "End_Point_IP";
    }
    
    public static String getCONFIG_ENPOINT_PORT() {
        return "End_POint_Port";
    }
    
    public static String getCONFIG_SP_ID() {
        return "Service_Provider_ID";
    }
    
    public static String getCONFIG_SID() {
        return "Service_ID";
    }
    
    public static String getCONFIG_SP_PASSWORD() {
        return "Service_Provider_Password";
    }
    
    public static String getCONFIG_SP_SHORTCODE() {
        return "Service_Provider_ShortCode";
    }
    
    public static String getCONFIG_SP_LONGCODE() {
        return "Service_Provider_LongCode";
    }
    
    public static String getCONFIG_STARTNOTIFYFLAG() {
        return "Start_NOtify_Flag";
    }
    
    public static String getCONFIG_SERVICE_OA() {
        return "Service_OA";
    }
    
    public static String getEnd_POINT_SMS_NOTIFICATION() {
        return "End_POINT_SMS_NOTIFICATION";
    }
    
    public static String getEnd_POINT_SMS_NOTIFICATION_2() {
        return "End_POINT_SMS_NOTIFICATION_2";
    }
    
    public static String getCONFIG_SERVICE_FA() {
        return "Service_FA";
    }
    
    public static String getCONFIG_HTTP_TYPE() {
        return "Http_Type";
    }
    
    public static String getCONFIG_SDP_IP() {
        return "Sdp_IP";
    }
    
    public static String getCONFIG_SDP_TEST_IP() {
        return "Sdp_IP_TEST";
    }
    
    public static String getCONFIG_SDP_PORT() {
        return "Sdp_Port";
    }
    
    public static String getSPP_SPID() {
        return "spp_spid";
    }
    
    public static String getSMS_CORELLATOR() {
        return "sms_corellator";
    }
    
    public static String getSPP_SPID_MOMO() {
        return "spp_spid_momo";
    }
    
    public static String getSPP_SPID_USSD() {
        return "spp_spid_ussd";
    }
    
    public static String getServiceID_MOMO() {
        return "Service_ID_MOMO";
    }
    
    public static String getServiceID_USSD() {
        return "Service_ID_USSD";
    }
    
    public static String getPassword_MOMO() {
        return "spp_password_momo";
    }
    
    public static String getPassword_USSD() {
        return "spp_password_ussd";
    }
    
    public static String getSPP_PASSWORD() {
        return "spp_password";
    }
    
    public static String getUser_HTTP_TYPE() {
        return "Http_Type_User";
    }
    
    public static String getUser_PORT() {
        return "port_User";
    }
    
    public static String getUser_IP() {
        return "Ip_User";
    }
    
    public static String getSUBSCRIBE_IP() {
        return "Subcribe_IP";
    }
    
    public static String getSUBSCRIBE_PORT() {
        return "Subcribe_port";
    }
    
    public static String getPRODUCT_ID() {
        return "product_ID";
    }
    
    public static String getOPER_CODE() {
        return "OperCode";
    }
    
    public static String getHTTP_TYPE_SUBSCRIBE() {
        return "Http_Type_Subscribe";
    }
    
    public static String getProduct_ID_Day() {
        return "product_ID_Day";
    }
    
    public static String getProduct_ID_Week() {
        return "product_ID_Week";
    }
    
    public static String getProduct_ID_Month() {
        return "product_ID_Month";
    }
    
    public static String getCONFIG_SDP_Number() {
        return "SDP_Number";
    }
    
    public static String getCONFIG_SDP_Key_Number() {
        return "SDP_KeyNumber";
    }
    
    public static String getStart_Info_Trafic() {
        return "Start_Info_Trafic";
    }
    
    public static String getSPP_SPID_ONDEMAND_TEST() {
        return "spp_spid_OnDemand_Test";
    }
    
    public static String getCONFIG_SDP_IP_TEST() {
        return "Sdp_IP_TEST";
    }
    
    public static String getSPP_SPID_SMS() {
        return "spp_spid_sms";
    }
    
    public static String getCONFIG_SDP_Key_Number_Production() {
        return "SDP_KeyProduction";
    }
    
    public static String getSDP_KeyProduction_Souscription() {
        return "SDP_KeyProduction_Souscription";
    }
    
    static {
        TemplatesUSSD.LENGTHBCRYT = 4;
        TemplatesUSSD.LENGTDATE = 6;
        TemplatesUSSD.BASE_USER_POSITION_ON_STRING_USSD = getBaseSeparateurChoice() + "INIT";
    }
}
