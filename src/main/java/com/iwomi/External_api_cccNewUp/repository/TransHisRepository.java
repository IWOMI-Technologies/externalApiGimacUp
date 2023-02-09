/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.TransactionHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface TransHisRepository extends JpaRepository<TransactionHistory, Long> {

    @Query(value = "UPDATE thisty set status =?1 where id_reference = ?2", nativeQuery = true)
    public void updateSTatus(String status, String trans_key);
    List<TransactionHistory> findByIdReference(String referenceId);
    
    @Query(value = "SELECT * FROM thisty e WHERE e.external_id = ?1",nativeQuery=true)
    public TransactionHistory findByPnbr(String pnbr);
    
    @Query(value = "SELECT e.* FROM thisty e",nativeQuery=true)
    List<TransactionHistory> findall();
    
    @Query(value = "SELECT * FROM thisty e WHERE id_reference = ?1", nativeQuery = true)
    TransactionHistory findBy_Reference_id(String referenceId);
   
}
