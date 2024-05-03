package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerTcdW;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerTcdWRepo extends JpaRepository<PartnerTcdW,Integer> {
    List<PartnerTcdW> findActive();
}
