/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.model;

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

public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;
	
	private String name;
	private String pwd;
	
	//default constructor for JSON Parsing
	

	private String getName() {
		return this.name;
	}

	private void setName(String name) {
		this.name = name;
	}

	private String getPwd() {
		return this.pwd;
	}

	private void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
