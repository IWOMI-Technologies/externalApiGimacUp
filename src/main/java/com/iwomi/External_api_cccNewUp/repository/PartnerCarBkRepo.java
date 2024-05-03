package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerCarBk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerCarBkRepo extends JpaRepository<PartnerCarBk,Integer> {
    List<PartnerCarBk> findActive();
}
