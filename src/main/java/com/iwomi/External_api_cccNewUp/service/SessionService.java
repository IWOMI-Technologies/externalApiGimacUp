package com.iwomi.External_api_cccNewUp.service;

import com.iwomi.External_api_cccNewUp.Entities.UserSession;
import com.iwomi.External_api_cccNewUp.repo.UserSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    @Autowired
    UserSessionRepo sessionRepo;

    public UserSession getSessionByPhoneAndSsid(String phone, String sessionId) {
        return sessionRepo.findClientByPhoneAndUuid(phone, sessionId);
    }

    public UserSession saveSession(UserSession session) {
        return sessionRepo.save(session);
    }
}
