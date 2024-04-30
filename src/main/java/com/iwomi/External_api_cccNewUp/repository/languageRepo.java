package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Language;
import com.iwomi.External_api_cccNewUp.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface languageRepo extends JpaRepository<Language, Integer> {
    @Query("from Language where active=1")
    List<Language> findActive();
}
