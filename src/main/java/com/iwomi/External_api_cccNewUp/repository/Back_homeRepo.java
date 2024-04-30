package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Back_home;
import com.iwomi.External_api_cccNewUp.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Back_homeRepo extends JpaRepository<Back_home, Integer> {
    @Query("from Back_home where active=1")
    List<Back_home> findActive();
}
