package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Cmaccount;
import com.iwomi.External_api_cccNewUp.model.PartnerCmBk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartnerCmBkRepo extends JpaRepository<PartnerCmBk,Integer> {
    @Query("from PartnerCmBk where active=1")
    List<PartnerCmBk> findActive();
}
