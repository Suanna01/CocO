package com.example.CocO.service.social;

import org.springframework.stereotype.Component;

@Component
public class NaverOauth implements SocialOauth {
    @Override
    public String getOauthRedirectURL() {
        return "";
    }

    @Override
    public String requestAccessToken(String code) {
        return null;
    }
}