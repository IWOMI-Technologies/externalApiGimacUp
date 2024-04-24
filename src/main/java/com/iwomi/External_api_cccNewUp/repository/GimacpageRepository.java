package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Homepage;
import com.iwomi.External_api_cccNewUp.model.gimacpage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GimacpageRepository extends JpaRepository<gimacpage,Integer> {
    @Query("from  gimacpage  where active =1")
    List<gimacpage> findActive();

    @Query(value = "SELECT * FROM gimacpage e WHERE e.rang = ?1 and e.active = ?2",nativeQuery=true)
    public gimacpage findByRang(String userchoice, String active);
}
