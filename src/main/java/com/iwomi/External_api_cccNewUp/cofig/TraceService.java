/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.cofig;

/**
 *
 * @author user
 */
public class TraceService {
    public static String name = ""; 
  
    // variable of type String 
    public String s; 
  
    // private constructor restricted to this class itself 
    public TraceService( String name) 
    { 
        s = name;
        this.name = name;
    } 
  
    // static method to create instance of Singleton class 
    public String getInstance() 
    { 
        return this.name; 
    } 

    public TraceService() {
    }
}

