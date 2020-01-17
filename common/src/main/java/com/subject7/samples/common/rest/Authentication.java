package com.subject7.samples.common.rest;

public class Authentication {
    private String username;
    private String password;
    private AuthenticationType authenticationType;

    public Authentication(String username, String password, AuthenticationType authenticationType) {
        this.username = username;
        this.password = password;
        this.authenticationType = authenticationType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }
}
