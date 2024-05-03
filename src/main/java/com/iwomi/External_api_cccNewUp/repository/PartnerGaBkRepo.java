package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerGaBk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerGaBkRepo extends JpaRepository<PartnerGaBk,Integer> {
    List<PartnerGaBk> findActive();
}
