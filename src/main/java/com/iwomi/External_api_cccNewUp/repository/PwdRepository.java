/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Pwd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author user
 */
public interface PwdRepository extends JpaRepository<Pwd, Long>{

    @Query(value = "SELECT * FROM pwd e WHERE e.acscd = ?1  and e.dele= ?2 ",nativeQuery=true)
    public Pwd findByAcscd(String acscd, String del);
    
}