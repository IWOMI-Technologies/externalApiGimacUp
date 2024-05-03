package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerCmBk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerCmBkRepo extends JpaRepository<PartnerCmBk,Integer> {
    List<PartnerCmBk> findActive();
}
