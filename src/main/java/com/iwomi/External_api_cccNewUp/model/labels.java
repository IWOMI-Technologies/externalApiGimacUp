/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
@Table(name = "labels")
public class labels implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public int id;
    @Column(name="cle", unique=true )
    public String key;//key
    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String valen;//pass
    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String valfr; 
    public String crtd;
    public String mdfi;
    public String dele;

  
    
}

