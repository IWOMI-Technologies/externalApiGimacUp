package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepo extends JpaRepository<Loans, Integer> {
    @Query("from  Loans  where active =1")
    List<Loans> findActive();

    @Query(value = "SELECT * FROM loans e WHERE e.rang = ?1 and e.active = ?2",nativeQuery=true)
    public Loans findByRang(String userchoice, String active);
}
