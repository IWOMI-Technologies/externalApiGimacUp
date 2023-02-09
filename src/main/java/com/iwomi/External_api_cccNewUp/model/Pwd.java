/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pwd")
public class Pwd implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public int id;
    public String login;//user
    public String pass;//pass
    public String descrip; 
    public String etab;
    public String acscd;
    public String lib1;//ip
    public String lib2;//port
    public String crtd;
    public String mdfi;
    public String dele;

    public Pwd(String login, String pass, String descrip, String etab, String acscd, String lib1, String lib2, String crtd, String mdfi, String dele) {
        this.login = login;
        this.pass = pass;
        this.descrip = descrip;
        this.etab = etab;
        this.acscd = acscd;
        this.lib1 = lib1;
        this.lib2 = lib2;
        this.crtd = crtd;
        this.mdfi = mdfi;
        this.dele = dele;
    }

    
   
}
