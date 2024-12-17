package com.example.CocO.dto.response;

public class UserResponse {
    private String name;
    private String accessToken;
    private String provider;

    public UserResponse(String name, String accessToken, String provider) {
        this.name = name;
        this.accessToken = accessToken;
        this.provider = provider;
    }

    // Getter, Setter 추가
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
