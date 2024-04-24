package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Ftsltranspage;
import com.iwomi.External_api_cccNewUp.model.gimacpage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FtsltransRepository extends JpaRepository<Ftsltranspage,Integer> {
    @Query("from  Ftsltranspage  where active =1")
    List<Ftsltranspage> findActive();

    @Query(value = "SELECT * FROM Ftsltranspage e WHERE e.rang = ?1 and e.active = ?2",nativeQuery=true)
    public Ftsltranspage findByRang(String userchoice, String active);
}
