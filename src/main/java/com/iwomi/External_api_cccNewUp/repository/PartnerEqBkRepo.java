package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.PartnerEqBk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerEqBkRepo extends JpaRepository<PartnerEqBk,Integer> {
    List<PartnerEqBk> findActive();
}
