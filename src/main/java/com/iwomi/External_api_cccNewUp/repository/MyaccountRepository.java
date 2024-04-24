package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Ftsltranspage;
import com.iwomi.External_api_cccNewUp.model.Myaccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MyaccountRepository extends JpaRepository<Myaccount, Integer> {
    @Query("from  Myaccount  where active =1")
    List<Myaccount> findActive();

    @Query(value = "SELECT * FROM myaccount e WHERE e.rang = ?1 and e.active = ?2",nativeQuery=true)
    public Myaccount findByRang(String userchoice, String active);
}
