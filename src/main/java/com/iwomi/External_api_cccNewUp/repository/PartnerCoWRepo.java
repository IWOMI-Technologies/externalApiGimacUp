package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerCarBk;
import com.iwomi.External_api_cccNewUp.model.PartnerCarW;
import com.iwomi.External_api_cccNewUp.model.PartnerCoW;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerCoWRepo extends JpaRepository<PartnerCoW,Integer> {
    List<PartnerCoW> findActive();
}
