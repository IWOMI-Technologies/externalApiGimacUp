package com.iwomi.External_api_cccNewUp.model;


import jakarta.persistence.*;
import org.json.JSONObject;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "wsocket")
public class WsocketModel implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String message;
    private String sender;
    private String usadm;
    private String message2;
    private Timestamp dou;//date create
    private Timestamp  dmo;//date update
    private String chl1; // champs libre
    private String chl2; // champs libre
    private String chl3; // champs libre
    private String chl4; // champs libre

    public WsocketModel() {
    }
//

    public WsocketModel(JSONObject jsonObject) {
        this.message = jsonObject.get("message").toString();
        this.sender = jsonObject.get("sender").toString();
        this.message2 = jsonObject.get("message2").toString();
        this.dou =new Timestamp(System.currentTimeMillis());
        this.dmo =new Timestamp(System.currentTimeMillis());
        // textMessageDTORepository.save(this);
    }



    public WsocketModel(String content) {
        this.message2 = content;
    }
    public WsocketModel(String message, String from, String text) {
        this.message = message;
        this.sender = from;
        this.message2 = text;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getMessage2() {
        return message2;
    }

    public void setMessage2t(String text) {
        this.message2 = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Timestamp getDou() {
        return dou;
    }

    public void setDou(Timestamp dou) {
        this.dou = dou;
    }

    public Timestamp getDmo() {
        return dmo;
    }

    public void setDmo(Timestamp dmo) {
        this.dmo = dmo;
    }

    public String getChl1() {
        return chl1;
    }

    public void setChl1(String chl1) {
        this.chl1 = chl1;
    }

    public String getChl2() {
        return chl2;
    }

    public void setChl2(String chl2) {
        this.chl2 = chl2;
    }

    public String getChl3() {
        return chl3;
    }

    public void setChl3(String chl3) {
        this.chl3 = chl3;
    }

    public String getChl4() {
        return chl4;
    }

    public void setChl4(String chl4) {
        this.chl4 = chl4;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUsadm() {
        return usadm;
    }

    public void setUsadm(String usadm) {
        this.usadm = usadm;
    }

    public WsocketModel(Long id, String message, String sender, String usadm, String text, Timestamp dou, Timestamp dmo, String chl1, String chl2, String chl3, String chl4) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.usadm = usadm;
        this.message2 = text;
        this.dou = dou;
        this.dmo = dmo;
        this.chl1 = chl1;
        this.chl2 = chl2;
        this.chl3 = chl3;
        this.chl4 = chl4;
    }






}

