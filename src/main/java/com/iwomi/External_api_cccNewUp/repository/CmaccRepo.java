package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Cmaccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CmaccRepo extends JpaRepository<Cmaccount, Integer> {
    @Query("from Cmaccount where active=1")
    List<Cmaccount> findActive();
}
