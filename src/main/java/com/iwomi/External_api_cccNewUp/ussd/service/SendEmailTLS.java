
package com.iwomi.External_api_cccNewUp.ussd.service;
import com.iwomi.External_api_cccNewUp.model.Nomenclature;
import com.iwomi.External_api_cccNewUp.model.Pwd;
import com.iwomi.External_api_cccNewUp.repository.NomenclatureRepository;
import com.iwomi.External_api_cccNewUp.repository.PwdRepository;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import org.springframework.mail.javamail.MimeMessageHelper;

/**
 *
 * @author TAGNE
 */
@Service
public class SendEmailTLS {
       ////final String username = "patricetagne@gmail.com";
       //final String password = "tbcyjlzfbemefcmm";
        final String username = "no_reply.mail@firsttrust.cm";
       final String password = "Ftsl2023";
       String uri = "sendEmail";

    String dele = "0";
     @Autowired
    private NomenclatureRepository nomenclatureRepository;
     @Autowired
    PwdRepository pwdRepository;
//      @Autowired
//    private JavaMailSender mailSender;
    

    public JSONObject  sendEmailApi1( String email, String sms, String obj){
        String tabcd="0012"; String acscd="0220"; //nm.getLib2()
        Nomenclature nm= nomenclatureRepository.findUrl1(tabcd, acscd, dele);
        String url1 = nm.getLib2() + "sendEmail";
       // Pwd pwd = pwdRepository.findByAcscd("0214","0");
//        JSONObject rr = 
//        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass().toString()));
//        String v = new String(decoder);
        Map<String, String> request = new HashMap();
        request.put("ema", email);
        request.put("obj", obj);
        request.put("msg", sms);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // headers.add("Authorization", "Bearer " + token);
        HttpEntity<Map> entity = new HttpEntity<Map>(request, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url1, entity, String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            return res;
        } else if (response.getStatusCodeValue() == 401) {
            System.out.println("token error:unauthorized");
            return null;//System.out.prinln("token error:"+response.getBody())unauthorized";
        } else if (response.getStatusCodeValue() == 500) {
            System.out.println("token error:" + response.getBody());
            return null;//"internal server error";
        } else {
            String result = response.getBody();
            System.out.println("token error:" + response.getBody());
            return null;//result;
        }
    }
  
    public JSONObject  sendEmailApi( String email, String sms, String obj){
        // change accordingly 
         JSONObject response = new JSONObject();
        String to = email;
        Pwd pwd = pwdRepository.findByAcscd("0214", "0");
        byte[] decoder = Base64.getDecoder().decode(pwd.getPass());
        String v = new String(decoder);
        final String mail_Server = pwd.getLib1();
        final String user = pwd.getLogin();
        final String pass = v;
        final String port = pwd.getLib2();
        // change accordingly 
        String from = user;

        // or IP address 
        //String host = "webmail.firsttrust.cm";
          String host = mail_Server;

        // mail id 
       // final String username1 = "no_reply.mail@firsttrust.cm";
         final String username1 = user;

        // correct password for gmail id 
        final String password1 = pass;

        System.out.println("TLSEmail Start: "+user);
        // Get the session object 

        // Get system properties 
        Properties properties = System.getProperties();

        // Setup mail server 
        properties.setProperty("mail.smtp.host", host);

        // SSL Port 
        properties.put("mail.smtp.port", port);
        // enable authentication 
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        // SSL  Factory 
        //properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        // creating Session instance referenced to  
        // Authenticator object to pass in  
        // Session.getInstance argument 
         Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username1, password1);
                    }
                });
     String st="Fail";
        //compose the message 
        try {
            // javax.mail.internet.MimeMessage class is mostly  
            // used for abstraction. 
            MimeMessage message = new MimeMessage(session);

            // header field of the header. 
            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            message.setSubject(obj);
            message.setText(sms);

            // Send message 
            Transport.send(message);
             response.put("'success", "01");
            response.put("message", "message successfully send");
          //  response.put("data", "");
            System.out.println("Yo it has been sent..");
        } catch (MessagingException mex) {
             response.put("'success", "100");
            response.put("message", "message not send, please try again later");
           // response.put("data", payload);
            mex.printStackTrace();
        }
        return response;
    }

    
}
