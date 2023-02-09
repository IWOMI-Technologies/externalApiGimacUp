/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwomi.External_api_cccNewUp.model.Pwd;
import com.iwomi.External_api_cccNewUp.model.TransactionHistory;
import com.iwomi.External_api_cccNewUp.repository.PwdRepository;
import com.iwomi.External_api_cccNewUp.repository.TransHisRepository;
import com.iwomi.External_api_cccNewUp.serviceInterface.MomoCollectionAPI;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.apache.commons.lang3.StringUtils.trim;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RestController
@CrossOrigin()
public class MomoOperationsController {

    @Autowired
    MomoCollectionAPI apiOperationsMTN;

    @Autowired
    TransHisRepository transHisRepository;

    @Autowired
    PwdRepository pwdRepository;

    TransactionHistory transactiomHistory;

    @RequestMapping(value = "/OperationStatus", method = RequestMethod.POST)
    public String OperationStatus(@RequestBody Map<String, String> payload) throws Exception {
        return apiOperationsMTN.OperationStatus(payload.get("referenceId"), payload.get("subKey"), payload.get("token"));

    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public Map<String, String> makeRedrawal(@RequestBody Map<String, String> payload)
            throws Exception {
        return apiOperationsMTN.performRedrawal(payload.get("amount"), payload.get("number"), payload.get("payerMsg"), payload.get("payeeMsg"), payload.get("currency"), payload.get("token"), payload.get("subKey"), "");

    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public String getToken(@RequestBody Map<String, String> payload)
            throws Exception {
        return apiOperationsMTN.getToken(payload.get("apiUser"), payload.get("apiKey"), payload.get("subKey"));

    }

    @RequestMapping(value = "/accountMomoStatus", method = RequestMethod.POST)
    public String MomoAccountExist(@RequestBody Map<String, String> payload)
            throws Exception {
        return apiOperationsMTN.MomoAccountExist(payload.get("number"), payload.get("subKey"), payload.get("token"));
    }

    @RequestMapping(value = "/accountBalance", method = RequestMethod.POST)
    public String getAccountBalance(@RequestBody Map<String, String> payload)
            throws Exception {
        return apiOperationsMTN.getAccountBalance(payload.get("subKey"), payload.get("token"));

    }

    @RequestMapping(value = "/sendSMS", method = RequestMethod.POST)
    public Map<String, Object> sendSMS(@RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap();
        String tel = payload.get("tel");
        String msg = payload.get("msg");

        String uri = "http://51.254.22.101/sms/ss/api.php?login=233431645&password=ccc19@2019&sender_id=CCC-Plc&destinataire=" + tel + "&message=" + msg;

        Map<String, String> request = new HashMap();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());

        if (response.getStatusCodeValue() == 200) {
            //success
            System.out.println(response.getBody());
            result.put("success", "01");
            result.put("message", "SMS successfully send");
            result.put("data", response.getBody());
            return result;
        } else {
            result.put("success", "100");
            result.put("message", "SMS not send");
            result.put("data", payload);
            return null;
        }

    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @RequestMapping(value = "/airtime", method = RequestMethod.POST)
    public Map<String, Object> airtime(@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, JsonProcessingException {
        Map<String, Object> result = new HashMap();
        String uri = "https://www.softeller.com/api_softeller/airtimeTopUp/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MessageDigest md = MessageDigest.getInstance("MD5");
        String usid = "209";
        byte[] messageDigest = md.digest(usid.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        payload.put("Login", "237676351813");
        String sr = sha256(hashtext + "000000");
        System.out.println("connection on an elmen ! " + sr);
        payload.put("Password", sr);
        JSONObject r = new JSONObject(payload);
        System.out.println("connection on an elmentt ! " + r);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        if (response.getStatusCodeValue() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            String json = response.getBody();
            // convert JSON string to Map
            Map<String, String> map = mapper.readValue(json, Map.class);
            // it works
            System.out.println(map);
            if (map.get("success").equals("01")) {
                System.out.println(response.getBody());
                result.put("success", "01");
                result.put("message", "credit successfully send");
                result.put("data", map);
                return result;
            } else {
                System.out.println(response.getBody());
                result.put("success", "100");
                result.put("message", "credit not send");
                result.put("data", map.get("message"));
                return result;
            }
        } else {
            result.put("success", "100");
            result.put("message", "credit not send");
            result.put("data", payload);
            result.put("data", response.getBody());
            return null;
        }
    }

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public Map<String, Object> sendEmail(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap();
        Pwd pwd = pwdRepository.findByAcscd("0214", "0");
        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass().toString()));
        String v = new String(decoder);
        final String mail_Server = trim(pwd.getLib1());
        final String user = trim(pwd.getLogin());
        final String pass = trim(v);
        final String port = pwd.getLib2();

        String msg = payload.get("msg");
        String title = payload.get("obj");
        String email = payload.get("ema");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", mail_Server);
        prop.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.ssl.trust", "*");
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject(title);
            message.setText(msg);

            Transport.send(message);
            response.put("'success", "01");
            response.put("message", "message successfully send");
            response.put("data", payload);
            System.out.println("Done");
        } catch (MessagingException e) {
            response.put("'success", "100");
            response.put("message", "message not send, please try again later");
            response.put("data", payload);
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = "/mailUpload", method = RequestMethod.POST)
    public Map<String, Object> mailUpload(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap();
        Pwd pwd = pwdRepository.findByAcscd("0214", "0");
        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass().toString()));
        String v = new String(decoder);
        final String mail_Server = trim(pwd.getLib1());
        final String user = trim(pwd.getLogin());
        final String pass = trim(v);
        final String port = pwd.getLib2();

        String msg1 = payload.get("msg");
        String title = payload.get("obj");
        String email = payload.get("ema");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", mail_Server);
        prop.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.ssl.trust", "*");
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                });

        try {

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(user));
            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            msg.setSubject(title);
//            message.setText(msg);
//            message.setContent(msg, "text/html");
//            Multipart multipart = new MimeMultipart();
//            MimeBodyPart textBodyPart = new MimeBodyPart();
//            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
//
//            String file = "http://localhost/mosaweb/ressources/img/logo_mosa.png";
//            String fileName = "attachmentName";
//
//            DataSource source = new FileDataSource(file);
//            
//            attachmentBodyPart.setDataHandler(new DataHandler(source));
//            attachmentBodyPart.setFileName(fileName);
//
//            textBodyPart.setContent(msg1, "text/html");
//
//            multipart.addBodyPart(textBodyPart);
//            multipart.addBodyPart(attachmentBodyPart);
//
//            msg.setContent(multipart);
//
//            Transport.send(msg);
//C:\wamp64\www\ccc_plc\admin\ressources\img
//            String file = "http://localhost/mosaweb/ressources/img/logo_mosa.png";
            String file = "C:/wamp64/www/mosaweb/ressources/img/logo_mosa.png";
            String fileName = "logo_mosa.png";

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
//            textBodyPart.setText("your text");
            textBodyPart.setContent(msg1, "text/html");

//            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
//            DataSource source = new FileDataSource(file); // ex : "C:\\test.pdf"
//            attachmentBodyPart.setDataHandler(new DataHandler(source));
//            attachmentBodyPart.setFileName(fileName); // ex : "test.pdf"

            multipart.addBodyPart(textBodyPart);  // add the text part
//            multipart.addBodyPart(attachmentBodyPart); // add the attachement part

            msg.setContent(multipart);

            Transport.send(msg);

            response.put("'success", "01");
            response.put("message", "message successfully send");
            response.put("data", payload);
            System.out.println("Done");
        } catch (MessagingException e) {
            response.put("'success", "100");
            response.put("message", "message not send, please try again later");
            response.put("data", payload);
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = "/emailConfig", method = RequestMethod.POST)
    public Map<String, Object> emailConfig(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap();
        Pwd pwd = pwdRepository.findByAcscd("0214", "0");
        pwd.setPass(Base64.getEncoder().encodeToString(payload.get("pass").getBytes()));
        pwd.setLib1(payload.get("smmail"));
        pwd.setLib2(payload.get("port"));
        pwd.setLogin(payload.get("login"));
        Pwd r = pwdRepository.save(pwd);
        if (r == null) {
            response.put("success", "0");
            return response;
        } else {
            response.put("success", "1");
            return response;
        }
    }

//    @RequestMapping(value = "/getpdf1", method = RequestMethod.GET)
//    public ResponseEntity<byte[]> getPDF1() {
//
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setContentType(MediaType.parseMediaType("application/pdf"));
//        String filename = "pdf1.pdf";
//
//        headers.add("content-disposition", "inline;filename=" + filename);
//
//        headers.setContentDispositionFormData(filename, filename);
//        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdf1Bytes, headers, HttpStatus.OK);
//        return response;
//    }
    
//     @RequestMapping(value = "/preview.pdf", method = RequestMethod.GET)
//protected String preivewSection(      
//    HttpServletRequest request,
//        HttpSession httpSession,
//    HttpServletResponse response) {
//    try {
//        byte[] documentInBytes = getDocument();         
//        response.setHeader("Content-Disposition", "inline; filename=\"report.pdf\"");
//        response.setDateHeader("Expires", -1);
//        response.setContentType("application/pdf");
//        response.setContentLength(documentInBytes.length);
//        response.getOutputStream().write(documentInBytes);
//    } catch (Exception ioe) {
//    } finally {
//    }
//    return null;
//}
}