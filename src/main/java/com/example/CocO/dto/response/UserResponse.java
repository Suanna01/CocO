package com.example.CocO.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private String name;
    private String provider;
    private String accessToken;
    private String jwtToken;
}
