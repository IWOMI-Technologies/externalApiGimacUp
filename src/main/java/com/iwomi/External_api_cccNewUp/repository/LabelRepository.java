/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.labels;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author user
 */
public interface LabelRepository extends JpaRepository<labels, Long>{

    @Query(value = "SELECT * FROM labels e WHERE e.cle = ?1 and e.dele = ?2",nativeQuery=true)
    public labels findByKey(String key, String dele);
    
    @Query(value = "SELECT * FROM labels e WHERE e.dele = ?1  ",nativeQuery=true)
    public List<labels> findALL(String dele);
    
}