/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.ussd.utils;

import com.iwomi.External_api_cccNewUp.model.responses;
import com.iwomi.External_api_cccNewUp.ussd.config.TemplatesUSSD;
import jakarta.xml.soap.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



/**
 *
 * @author user
 */
@Configuration
public class UtilsUssd implements EnvironmentAware
{
    private static Environment env;
    private static MessageDigest MD;
    public static String timestamp;
    
    public static String getPropertiesValue(final String properties) {
        return UtilsUssd.env.getProperty(properties);
    }
    
    public void setEnvironment(final Environment environment) {
        UtilsUssd.env = environment;
    }
    
    public static final String generateMD5(final String spid, final String password, final String timestamp) {
        final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            final String s = spid + password + timestamp;
            final byte[] strTemp = s.getBytes();
            UtilsUssd.MD.update(strTemp);
            final byte[] md = UtilsUssd.MD.digest();
            final int j = md.length;
            final char[] str = new char[j * 2];
            int k = 0;
            for (final byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xF];
                str[k++] = hexDigits[byte0 & 0xF];
            }
            return new String(str);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static String getMessage1SeparatorMessage2() {
        return "@--@";
    }
    
    public static String getDateInString() {
        final SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeformat.setTimeZone(TimeZone.getTimeZone("UTC+2"));
        return timeformat.format(new Date());
    }
    
    public static String getBoosConnected() {
        return "boss is connected";
    }
    
    public static synchronized String formatDate(final long date, final String format, final boolean addHour) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date resultdate = new Date(date);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(resultdate);
        if (addHour) {
            cal.add(10, 1);
        }
        resultdate = cal.getTime();
        return sdf.format(resultdate);
    }
    
    public static String ExtrackUserphone(String PhoneUser) {
        if (PhoneUser.startsWith("+237") || PhoneUser.startsWith("237") || PhoneUser.startsWith("00237")) {
            PhoneUser = PhoneUser.split("7", 2)[1];
        }
        return PhoneUser.trim();
    }
    
    public static String getAtomicSpace() {
        return " ";
    }
    
    public static String getTronconSmsSeparator() {
        return " ";
    }
    
    public static String replaceEt(final String response) {
        return response.replaceAll("&", "&amp;");
    }
    
    public static String stripAccents(final String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    public static String returnChariot() {
        return System.getProperty("line.separator");
    }
    
    public static String ReplaceStringToAscii(final String values) {
        return values;
    }
    
    private static void disableSslVerification() {
        try {
        	TrustManager[] trustAllCerts = { new X509TrustManager() {
        		/*     */         public X509Certificate[] getAcceptedIssuers() {
        		/* 220 */           return null;
        		/*     */         }
        		/*     */ 
        		/*     */         public void checkClientTrusted(X509Certificate[] certs, String authType)
        		/*     */         {
        		/*     */         }
        		/*     */ 
        		/*     */         public void checkServerTrusted(X509Certificate[] certs, String authType)
        		/*     */         {
        		/*     */         }
        		/*     */       }
        		/*     */        };
        	SSLContext sc = SSLContext.getInstance("SSL");
        	/* 233 */       sc.init(null, trustAllCerts, new SecureRandom());
        	/* 234 */       HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        	/*     */ 
        	/* 237 */       HostnameVerifier allHostsValid = new HostnameVerifier() {
        	/*     */         public boolean verify(String hostname, SSLSession session) {
        	/* 239 */           return true;
        	/*     */         }
        	/*     */       };
        	/* 246 */       HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (KeyManagementException e2) {
            e2.printStackTrace();
        }
    }
    
    public static String trueorFalseFromInteger(final Integer active) {
        if (null == active) {
            return "nothing";
        }
        switch (active) {
            case 1: {
                return "yes";
            }
            case 0: {
                return "no";
            }
            default: {
                return "nothing";
            }
        }
    }
    
    public static synchronized String DateAddDay(final Date date, final String format, final Integer day) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, day);
        return sdf.format(cal.getTime());
    }
    
    public static String getCameroonNumber() {
        return "237";
    }
    
    public static String getCameroonNumberPlus() {
        return "237";
    }
    
    public static String getPlus() {
        return "";
    }
    
    public static String internationaliseNumber(final String number) {
        if (number.startsWith(getCameroonNumberPlus())) {
            return number;
        }
        if (number.startsWith(getCameroonNumber())) {
            return getPlus() + number;
        }
        return getCameroonNumberPlus() + number;
    }
    
    public static String[] splitString(final String dataToSplit, final String Splitsymbol) {
        return dataToSplit.split("\\" + Splitsymbol);
    }
    
    public static Double numberDigitAfterComa(double value, final int places) {
        final String nombre = ("" + value).split("\\.")[0];
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        Double factor = Math.pow(10.0, places);
        if (nombre.length() > 2) {
            factor = Math.pow(10.0, 1.0);
        }
        value *= factor;
        final long tmp = Math.round(value);
        return tmp / factor;
    }
    
    public static String StringToSHA256(final String valueToTransform) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(valueToTransform.getBytes());
            final byte[] byteData = md.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; ++i) {
                sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
    
    public String readFile(final InputStream in) {
        final StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(in);
            br = new BufferedReader(reader);
            for (String readLine = br.readLine(); readLine != null; readLine = br.readLine()) {
                sb.append(readLine);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
            catch (Exception ex) {}
        }
        return sb.toString();
    }
    
    private SOAPConnection getConnection() {
        SOAPConnection connection = null;
        try {
            final SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
            connection = soapConnFactory.createConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    private void closeConnection(final SOAPConnection connection) {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SOAPException e) {
                e.printStackTrace();
            }
        }
    }
    
    private DOMSource getDOMSource(final String xml) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        final DocumentBuilder builder = dbFactory.newDocumentBuilder();
        final ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        final Document document = builder.parse(bais);
        final DOMSource domSource = new DOMSource(document);
        return domSource;
    }
    
    private javax.swing.text.Document getxmlDoc(final String xml) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        final DocumentBuilder builder = dbFactory.newDocumentBuilder();
        final ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        final javax.swing.text.Document document = (javax.swing.text.Document)builder.parse(bais);
        return document;
    }
    
    private String extractResponse(final SOAPMessage reply) throws SOAPException, TransformerException {
        final StringWriter sw = new StringWriter();
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        final Source sourceContent = reply.getSOAPPart().getContent();
        final StreamResult result = new StreamResult(sw);
        transformer.transform(sourceContent, result);
        return sw.toString();
    }
    
    public static String getServiceUserId(final String phoneUser, final String troncon, final String other) {
        final String product_ID = "237012000017429";
        return product_ID;
    }
    
    public static String getServiceUserId_TEST(final String phoneUser, final String troncon, final String other) {
        final String product_ID = "237012000009011";
        return product_ID;
    }
    
    public Object doHttpPost(final String urlString, final String requestString) throws Exception {
        Object response = null;
        SOAPConnection con = null;
        try {
            final MessageFactory messageFactory = MessageFactory.newInstance();
            final SOAPMessage message = messageFactory.createMessage();
            final MimeHeaders headers = message.getMimeHeaders();
            final SOAPPart soapPart = message.getSOAPPart();
            soapPart.setContent(this.getDOMSource(replaceEt(requestString)));
            message.saveChanges();
            con = this.getConnection();
            final URL urlObj = new URL(urlString);
            response = con.call(message, urlObj);
            final String responses = this.extractResponse((SOAPMessage)response);
            System.out.println("Response From SDP :" + responses);
        }
        catch (Exception ex) {
            response = null;
            ex.printStackTrace();
        }
        finally {
            this.closeConnection(con);
        }
        return response;
    }
    
    public Object doHttpPostSMS(final String urlString, final String requestString) throws Exception {
        Object response = null;
        SOAPConnection con = null;
        try {
            final MessageFactory messageFactory = MessageFactory.newInstance();
            final SOAPMessage message = messageFactory.createMessage();
            final MimeHeaders headers = message.getMimeHeaders();
            final SOAPPart soapPart = message.getSOAPPart();
            soapPart.setContent(this.getDOMSource(replaceEt(requestString)));
            message.saveChanges();
            con = this.getConnection();
            final URL urlObj = new URL(urlString);
            response = con.call(message, urlObj);
            final String responses = this.extractResponse((SOAPMessage)response);
            System.out.println("Response From SDP :" + responses);
        }
        catch (Exception ex) {
           response = null;
            ex.printStackTrace();
        }
        finally {
            this.closeConnection(con);
        }
        return response;
    }
    
    public Object doHttpPostMomo(final String urlString, final String requestString) throws Exception {
        Object response = null;
        SOAPConnection con = null;
        try {
            final MessageFactory messageFactory = MessageFactory.newInstance();
            final SOAPMessage message = messageFactory.createMessage();
            final MimeHeaders headers = message.getMimeHeaders();
            final SOAPPart soapPart = message.getSOAPPart();
            soapPart.setContent(this.getDOMSource(replaceEt(requestString)));
            message.saveChanges();
            con = this.getConnection();
            final URL urlObj = new URL(urlString);
            response = con.call(message, urlObj);
            final String responses = this.extractResponse((SOAPMessage)response);
            System.out.println("Response From SDP :" + responses);
        }
        catch (Exception ex) {
            response = null;
            ex.printStackTrace();
        }
        finally {
            this.closeConnection(con);
        }
        return response;
    }
    
    public SOAPMessage CreateMessage(final String XMLToSend) {
        SOAPMessage message = null;
        try {
            final MessageFactory messageFactory = MessageFactory.newInstance();
            message = messageFactory.createMessage();
            final MimeHeaders headers = message.getMimeHeaders();
            final SOAPPart soapPart = message.getSOAPPart();
            soapPart.setContent(this.getDOMSource(XMLToSend));
            message.saveChanges();
        }
        catch (IOException | ParserConfigurationException | SOAPException | SAXException ex3) {
            ex3.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return message;
    }
    
    public String executeSoapRequest(final String soapUrlRequest, final String soapRequest, final String ReSynOderReques) throws IOException {
        try {
            final URL url = new URL(soapUrlRequest);
            final URLConnection conn = url.openConnection();
            conn.setRequestProperty("SOAPAction", soapUrlRequest);
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(soapRequest);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line0 = "";
            String line2;
            while ((line2 = rd.readLine()) != null) {
                line0 += line2;
            }
            System.out.println("first response by HTTP only " + line0);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(ReSynOderReques);
            wr.flush();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            line0 = "";
            while ((line2 = rd.readLine()) != null) {
                line0 += line2;
            }
            System.out.println("second response by HTTP only " + line0);
            return line0;
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
            return "error on data";
        }
    }
    
    public static int getRandomNumberInRange(final int min, final int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        final Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }
    
    public static String getDefaultInfotraficFreePassword() {
        return "12345";
    }
    
    public ResponseEntity<?> doHttpPostReSyncOrder(final String urlString, final String requestString, final String ReSynOderRequest, final String PhoneUser, final Boolean SubscribeOrNot) throws Exception {
        SOAPMessage response = null;
        final SOAPConnection con = this.getConnection();
        final URL urlObj = new URL(urlString);
        response = con.call(this.CreateMessage(requestString), urlObj);
        if (Objects.isNull(response)) {
            return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSubscriptionExistsString(), -1));
        }
        final String responses = this.extractResponse(response);
        if (responses.contains("Success") || responses.toLowerCase().contains("success")) {
            return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getSuccessString(), 1));
        }
        System.out.println("Souscription Error on SDP");
        return (ResponseEntity<?>)ResponseEntity.status(HttpStatus.OK).body((Object)new responses(TemplatesUSSD.getFailSubscriptionString(), -1));
    }
    
    public static Date StringToDateParsings(final String date) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(date);
        }
        catch (ParseException ex) {
            Logger.getLogger(UtilsUssd.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Date formatStringToDateMysqlTimeStamp(final String date) {
        Date startDate = null;
        final SimpleDateFormat dateFormat0 = new SimpleDateFormat("yyyyMMddHHmmss");
        final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startDate = getDateFromDateOtherFormat(dateFormat0.parse(date), "yyyy-MM-dd HH:mm:ss");
        }
        catch (ParseException ex2) {
            try {
                startDate = dateFormat2.parse(date);
            }
            catch (ParseException ex1) {
                Logger.getLogger(UtilsUssd.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(UtilsUssd.class.getName()).log(Level.SEVERE, null, ex2);
        }
        return startDate;
    }
    
    public static String getStringFromDate(final Date date, final String format) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
    
    public static Date getDateFromDateOtherFormat(final Date date, final String format) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateFormat.format(date));
        }
        catch (ParseException ex) {
            Logger.getLogger(UtilsUssd.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static int hoursDifference(final Date date1, final Date date2) {
        final int MILLI_TO_HOUR = 3600000;
        return (int)(date1.getTime() - date2.getTime()) / 3600000;
    }
    
    public static Date formatCurrentDateToTMysqlTimeStamp() {
        Date startDate = null;
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startDate = dateFormat.parse(dateFormat.format(new Date()));
        }
        catch (ParseException ex) {
            Logger.getLogger(UtilsUssd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return startDate;
    }
    
    public static String formatCurrentDateToStringMysqlTimeStamp() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String startDate = dateFormat.format(new Date());
        return startDate;
    }
    
    public static String generateTimeStamp(final String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(Calendar.getInstance().getTime());
    }
    
    public static String generateTimeStampGMT1(final String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        final Calendar cal = Calendar.getInstance();
        cal.add(10, 1);
        return sdf.format(cal.getTime());
    }
    
    
    public static Document loadXMLString(final String response) throws Exception {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final InputSource is = new InputSource(new StringReader(response));
        return db.parse(is);
    }
    
    public static String getResponseAbortNotifyUSSD() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\nxmlns:loc=\"http://www.csapi.org/schema/parlayx/ussd/notification/v1_0/local\">\n<soapenv:Header/>\n<soapenv:Body>\n<loc:notifyUssdAbortResponse/>\n</soapenv:Body>\n</soapenv:Envelope>";
    }
    
    public static String getResponseSendUssdAbort() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\nxmlns:loc=\"http://www.csapi.org/schema/parlayx/ussd/send/v1_0/local\">\n<soapenv:Header/>\n<soapenv:Body>\n<loc:sendUssdAbortResponse/>\n</soapenv:Body>\n</soapenv:Envelope>";
    }
    
    public static String getResponseReceiveUSSDDemand() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\nxmlns:loc=\"http://www.csapi.org/schema/parlayx/ussd/notification/v1_0/local\">\n<soapenv:Header/>\n<soapenv:Body>\n<loc:notifyUssdReceptionResponse>\n<loc:result>0</loc:result>\n</loc:notifyUssdReceptionResponse>\n</soapenv:Body>\n</soapenv:Envelope>";
    }
    
    public static String getResponseSMSNotify() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n                  xmlns:loc=\"http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local\">\n    <soapenv:Header/>\n    <soapenv:Body>\n        <loc:notifySmsDeliveryReceiptResponse/>\n    </soapenv:Body>\n</soapenv:Envelope>";
    }
    
    public static String getResponseResyncPaymentMomoSuccess() {
        return Factory.getEncodageMomo() + "<soapenv:Body>\n<requestPaymentCompletedResponse\nxmlns=\"http://www.csapi.org/schema/momopayment/local/v1_0\">\n<result>\n<resultCode xmlns=\"\">00000000</resultCode>\n<resultDescription xmlns=\"\">success</resultDescription>\n</result>\n<extensionInfo>\n<item xmlns=\"\">\n<key>result</key>\n<value>success</value>\n</item>\n</extensionInfo>\n</requestPaymentCompletedResponse>\n</soapenv:Body>\n</soapenv:Envelope>";
    }
    
    public static String getMessage(final String code, final String locale, final MessageSource messageSource) {
        return messageSource.getMessage(code, (Object[])null, new Locale(locale));
    }
    
    public static List<String> getFullNameFromXml(final String response, final String tagName) {
        List ids;
        try {
            final Document xmlDoc = loadXMLString(response);
            final NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
            ids = new ArrayList(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); ++i) {
                final Node x = nodeList.item(i);
                ids.add(x.getFirstChild().getNodeValue());
                System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
            }
        }
        catch (Exception ex) {
            ids = new ArrayList();
            ids.add("");
        }
        if (Objects.isNull(ids) || ids.size() == 0) {
            ids = new ArrayList();
            ids.add("");
        }
        return (List<String>)ids;
    }
    
    static {
        UtilsUssd.timestamp = "20151014110105";
        try {
            UtilsUssd.MD = MessageDigest.getInstance("MD5");
            disableSslVerification();
        }
        catch (Exception e) {
            throw new RuntimeException("Get MD5 instance failed.");
        }
    }
}
