/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Ussdfirstpage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author user
 */
public interface UssdfirstpageRepository extends JpaRepository<Ussdfirstpage, Integer>
{
    @Query("from  Ussdfirstpage  where active =1")
    List<Ussdfirstpage> findAllActive();
    
    @Query(value = "SELECT * FROM ussdfirstpage e WHERE e.rang = ?1 and e.active = ?2",nativeQuery=true)
    public Ussdfirstpage findByRang(String userchoice, String active);
}
