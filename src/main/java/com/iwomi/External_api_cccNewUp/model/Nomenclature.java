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
@Table(name ="sanm")
public class Nomenclature {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String tabcd;
	public String getTabcd() {
		return tabcd;
	}
	public void setTabcd(String tabcd) {
		this.tabcd = tabcd;
	}
	private String acscd;
	private int dele;
	private String lib1;
	private String lib2;
	private String lib3;
	private String lib4;
	private String lib5;
	private Long  taux1;
	private Long  taux2;
	private Long  taux3;
	private Long  taux4;
	private Long  taux5;
	private Long  mnt1;
	private Long  mnt2;
	private Long  mnt3;
	private Long  mnt4;
	private Long  mnt5;
	private Date dt1;
	private Date dt2;
	private Date dt3;
	private Date dt4;
	private Date dt5;
	private String crtd;
	private String mdfi;
	private String muser;
	private String cuser;
	private String cetab;
	
	public Boolean setByColumn(String column, String value) {
		switch (column) {
		case "lib1":
			this.lib1 = value;
			break;
			
		case "acscd":
			this.acscd = value;
			break;
		default:
			return false;
		}
		return true;
	
	}

	
	
}
