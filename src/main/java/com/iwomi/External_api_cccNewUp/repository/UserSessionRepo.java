
package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.UserSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author user
 */
public interface UserSessionRepo extends JpaRepository<UserSession, Long>{

    @Query(value = "SELECT * FROM ussdsession e WHERE e.phone = ?1",nativeQuery=true)
    public Optional<UserSession> findClientByPhone(String phone);
    
    @Query(value = "SELECT * FROM ussdsession e WHERE e.phone = ?1 and uuid =?2 ",nativeQuery=true)
    public UserSession findClientByPhoneAndUuid(String phone, String uuid);
    
}
