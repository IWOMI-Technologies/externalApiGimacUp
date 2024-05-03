package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerEqW;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerEqWRepo extends JpaRepository<PartnerEqW,Integer> {
    List<PartnerEqW> findActive();
}
