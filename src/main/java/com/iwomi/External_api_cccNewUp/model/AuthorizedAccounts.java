/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import java.util.Date;
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
@Table(name = "auth_acc")
public class AuthorizedAccounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
  
    @Column
    private String name;
    
    @Column
    private String pwd;
    
  
    @Column
    private String code;
    
    @Column
    @JsonIgnore
    private String descp;
    
     @Column
    @JsonIgnore
    private Date crdt;
    
    @Column
    @JsonIgnore
    private Date mdt;
    
    @Column
    @JsonIgnore
    private String chl1;
    
    @Column
    @JsonIgnore
    private String chl2;
    
    @Column
    @JsonIgnore
    private String chl3;
    
    @Column
    @JsonIgnore
    private String chl4;
    
    @Column
    @JsonIgnore
    private String chl5;
    public AuthorizedAccounts( String name, String pwd, String code, String descp, Date crdt, Date mdt) {
        
        this.name = name;
        this.pwd = pwd;
        this.code = code;
        this.descp = descp;
        this.crdt = crdt;
        this.mdt = mdt;
    }

    

    

    
}

