package com.iwomi.External_api_cccNewUp.repo;

import com.iwomi.External_api_cccNewUp.Entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserSessionRepo extends JpaRepository<UserSession, Integer> {
    @Query(value = "SELECT * FROM ussdsession e WHERE e.phone = ?1",nativeQuery=true)
    public Optional<UserSession> findClientByPhone(String phone);

    @Query(value = "SELECT * FROM ussdsession e WHERE e.phone = ?1 and uuid =?2 ",nativeQuery=true)
    public UserSession findClientByPhoneAndUuid(String phone, String uuid);
}
