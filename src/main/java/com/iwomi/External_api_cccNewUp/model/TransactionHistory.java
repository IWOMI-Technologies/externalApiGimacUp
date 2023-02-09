/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "thisty")
public class TransactionHistory {
    
     @Column
    private int amount;
    @Column
    private String currency;
    @Column
    private String name;
    @Column
    @Id
    private String externalId;
    @Column
    private String payer;
    @Column
    private String payerMessage;
    @Column
    private String payeeNote;
    @Column
    private String status;
    @Column
    private String idReference;
    @Column
    private String opty;//operation_type
    @Column
    private Date doc;//date of operation
    @Column
    private String pty;//payertype
    @Column
    private String reason;
    @Column
    private String cmptbkst;
    @Column
    private String codser;
    @Column
    private int cntSend;
    private int finTranId;
    private String date_modify;
    public TransactionHistory(int amount, String name,String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, Date date_create, String payertype, String reason, String cmptbkst, String codser, int finTranId, String date_modify) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = date_create;
        this.pty = payertype;
        this.reason = reason;
        this.cmptbkst = cmptbkst;
        this.codser = codser;
        this.finTranId = finTranId;
        this.date_modify = date_modify;
        this.name = name;
  }
    public TransactionHistory(int amount, String name, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, Date date_create, String payertype, String reason, String cmptbkst, int finTranId, String date_modify) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = date_create;
        this.pty = payertype;
        this.reason = reason;
        this.cmptbkst = cmptbkst;
        this.finTranId = finTranId;
        this.date_modify = date_modify;
        this.name = name;
        
    }
     public TransactionHistory(int amount, String name,String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, Date date_create, String payertype, String reason) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = date_create;
        this.pty = payertype;
        this.reason = reason;
        this.finTranId = finTranId;
        this.name = name;
    }
      public TransactionHistory(int amount, String name, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, String payertype, String reason, String date_modify) {
        this.amount = amount;
        this.currency = currency;
        this.date_modify = date_modify;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = doc;
        this.pty = payertype;
        this.reason = reason;
        this.finTranId = finTranId;
        this.name = name;
    }

    public TransactionHistory(String amount, String name,String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, String payertype, String reason, String date_modify) {
        this.amount = Integer.parseInt(amount);
        this.currency = currency;
        this.date_modify = date_modify;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = doc;
        this.pty = payertype;
        this.reason = reason;
        this.finTranId = finTranId;
        this.name = name;
    }
    public TransactionHistory(int i, String name, String test, String test0, String test1, String test2, String test3, String test4, String test5, String test6, Date date, String test7, String test8, int i0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
