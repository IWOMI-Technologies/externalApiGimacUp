package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Back_home;
import com.iwomi.External_api_cccNewUp.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountryRepo extends JpaRepository<Country, Integer> {
    @Query("from Country where active=1")
    List<Country> findActive();
}
