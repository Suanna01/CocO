package com.example.CocO.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String name;
    private String accessToken;
    private String provider;

    public UserResponse(String name, String accessToken, String provider) {
        this.name = name;
        this.accessToken = accessToken;
        this.provider = provider;
    }
}
