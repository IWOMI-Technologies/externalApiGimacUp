package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.Homepage;
import com.iwomi.External_api_cccNewUp.model.Ussdfirstpage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * @author user
 */
public interface HomepageRepository extends JpaRepository<Homepage, Integer> {
    @Query("from  Homepage  where active =2")
    List<Homepage> findActive();

    @Query(value = "SELECT * FROM Homepage e WHERE e.rang = ?1 and e.active = ?2",nativeQuery=true)
    public Homepage findByRang(String userchoice, String active);
}
