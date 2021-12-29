package net.proselyte.jwtappdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthTokenResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("login")
    private String login;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
