package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Country;
import com.iwomi.External_api_cccNewUp.model.PartnerCmW;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerCmWRepo extends JpaRepository<PartnerCmW, Integer> {

    List<PartnerCmW> findActive();
}
