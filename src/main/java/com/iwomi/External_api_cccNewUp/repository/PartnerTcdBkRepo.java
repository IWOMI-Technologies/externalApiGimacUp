package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerCmBk;
import com.iwomi.External_api_cccNewUp.model.PartnerTcdBk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerTcdBkRepo extends JpaRepository<PartnerTcdBk,Integer> {
    List<PartnerTcdBk> findActive();
}
