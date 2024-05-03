package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerCoBk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerCoBkRepo extends JpaRepository<PartnerCoBk,Integer> {
    List<PartnerCoBk> findActive();
}
