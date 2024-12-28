package com.evoluction.App_auth_keycloak.model;

import java.util.Map;

public class UserModel {
    private String username;
    private String email;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private Map<String, String>[] credentials;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<String, String>[] getCredentials() {
        return credentials;
    }

    public void setCredentials(Map<String, String>[] credentials) {
        this.credentials = credentials;
    }
}
