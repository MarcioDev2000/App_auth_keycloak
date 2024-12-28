package com.evoluction.App_auth_keycloak.services;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}