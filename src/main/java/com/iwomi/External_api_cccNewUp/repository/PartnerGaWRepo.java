package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerGaW;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerGaWRepo extends JpaRepository<PartnerGaW,Integer> {
    List<PartnerGaW> findActive();
}
