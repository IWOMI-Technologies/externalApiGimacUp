/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.MtnMomoConfig;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface MTNRepo extends JpaRepository<MtnMomoConfig, Long> {
 List<MtnMomoConfig> findByCode(String s);  
 	@Query(value = "SELECT e.* FROM mtn_info e", nativeQuery = true)
	List<MtnMomoConfig> findall();
        
        @Query(value = "SELECT * FROM mtn_info where code=?1", nativeQuery = true)
	MtnMomoConfig findMTNByCode(String code);
}
