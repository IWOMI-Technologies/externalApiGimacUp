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
import java.sql.Date;
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
@Table(name = "sause")
public class User implements Serializable {
	 @Id
	 @GeneratedValue(strategy= GenerationType.AUTO)
	 private Long id;

	 private Date crtd;

	 private Date mdfi;
	 private String sttus;
	 private Long acabs;
	 private String ttle;
	 private String lname;
	 private String fname;
	 private String matcl;
	 private String passw;
	 private String prfpc;
	 
	 private String gnder;
	 private Date birdy;
	 private String email;
	 private String uname;
	 private String phone;
	 private String regnm;
	 private String addrs;
	 private String ctry;
	 private Long subst;
	 private Date lstlg;
	 private Long cuser;
	 private Long muser;
	 
	 private String srvid;
	 private String inofis;
	 private String prfle;
	 private Long dycsm;
	 private String stycd;
	 private String hixdt;
	 private Long tvday;
	 private Long vardy;
	 private String csaut;
	 
	 private String canv;
	 private Long baln;
	 private String lcpdt;
	 private String brch;
	 private String cetab;
	 private String catg;
	 private Date aldx;
	 private Long adxtus;
	 private String fuctn;
         private String nfac;//numero de facturier


	public boolean setByUser1(User d) {
		if (d.lname != null) {
			this.lname = d.lname;
		}
		if (d.fname != null) {
			this.fname = d.fname;
		}
		if (d.addrs != null) {
			this.addrs = d.addrs;
		}
		if (d.birdy != null) {
			this.birdy = d.birdy;
		}
		if (d.brch != null) {
			this.brch = d.brch;
		}
		if (d.catg != null) {
			this.catg = d.catg;
		}
		if (d.csaut != null) {
			this.csaut = d.csaut;
		}
		if (d.ctry != null) {
			this.ctry = d.ctry;
		}
		if (d.cuser != null) {
			this.cuser = d.cuser;
		}
		if (d.email != null) {
			this.email = d.email;
		}
		if (d.fuctn != null) {
			this.fuctn = d.fuctn;
		}
		if (d.gnder != null) {
			this.gnder = d.gnder;
		}
		if (d.hixdt != null) {
			this.hixdt = d.hixdt;
		}
		if (d.matcl != null) {
			this.matcl = d.matcl;
		}
		if (d.muser != null) {
			this.muser = d.muser;
		}
		if (d.passw != null) {
			this.passw = d.passw;
		}
		if (d.phone != null) {
			this.phone = d.phone;
		}
		if (d.prfpc != null) {
			this.prfpc = d.prfpc;
		}
		if (d.regnm != null) {
			this.regnm = d.regnm;
		}
		if (d.srvid != null) {
			this.srvid = d.srvid;
		}
		if (d.subst != null) {
			this.subst = d.subst;
		}
		if (d.ttle != null) {
			this.ttle = d.ttle;
		}
		if (d.uname != null) {
			this.uname = d.uname;
		}
		return true;
	}

    public void setAcabs(int v1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	 
}

