package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Country;
import com.iwomi.External_api_cccNewUp.model.PartnerCmBk;
import com.iwomi.External_api_cccNewUp.model.PartnerCmW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartnerCmWRepo extends JpaRepository<PartnerCmW, Integer> {
    @Query("from PartnerCmW where active=1")
    List<PartnerCmW> findActive();
}
