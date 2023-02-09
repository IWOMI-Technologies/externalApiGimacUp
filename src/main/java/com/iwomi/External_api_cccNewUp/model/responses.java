/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.model;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class responses implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String response;
    private Integer coderesponse;
    
    public responses(final String errors, final Integer errorcode) {
        this.response = errors;
        this.coderesponse = errorcode;
    }
    
    public responses(final String errors) {
        this.response = errors;
        this.coderesponse = -1;
    }

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Integer getCoderesponse() {
		return coderesponse;
	}

	public void setCoderesponse(Integer coderesponse) {
		this.coderesponse = coderesponse;
	}
    
    
}

