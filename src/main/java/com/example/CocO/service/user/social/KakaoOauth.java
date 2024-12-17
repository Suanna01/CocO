package com.example.CocO.service.user.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth {

    @Value("${sns.kakao.url}")
    private String KAKAO_SNS_BASE_URL;
    @Value("${sns.kakao.client.id}")
    private String KAKAO_SNS_CLIENT_ID;
    @Value("${sns.kakao.callback.url}")
    private String KAKAO_SNS_CALLBACK_URL;
    @Value("${sns.kakao.client.secret}")
    private String KAKAO_SNS_CLIENT_SECRET;
    @Value("${sns.kakao.token.url}")
    private String KAKAO_SNS_TOKEN_BASE_URL;

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", KAKAO_SNS_CLIENT_ID);
        params.put("redirect_uri", KAKAO_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return KAKAO_SNS_BASE_URL + "?" + parameterString;
    }

    @Override
    public String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. HTTP 헤더 설정 (Content-Type을 application/x-www-form-urlencoded로 설정)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. 요청 파라미터를 문자열로 인코딩
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", KAKAO_SNS_CLIENT_ID);
        params.add("client_secret", KAKAO_SNS_CLIENT_SECRET);
        params.add("redirect_uri", KAKAO_SNS_CALLBACK_URL);
        params.add("grant_type", "authorization_code");

        // 3. HttpEntity 생성 (헤더와 파라미터 포함)
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // 4. POST 요청 보내기
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(KAKAO_SNS_TOKEN_BASE_URL, requestEntity, String.class);

        // 5. 응답 확인 및 반환
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return "카카오 로그인 요청 처리 실패";
    }


    public String requestAccessTokenUsingURL(String code) {
        try {
            URL url = new URL(KAKAO_SNS_TOKEN_BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            Map<String, Object> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", KAKAO_SNS_CLIENT_ID);
            params.put("client_secret", KAKAO_SNS_CLIENT_SECRET);
            params.put("redirect_uri", KAKAO_SNS_CALLBACK_URL);
            params.put("grant_type", "authorization_code");

            String parameterString = params.entrySet().stream()
                    .map(x -> x.getKey() + "=" + x.getValue())
                    .collect(Collectors.joining("&"));

            BufferedOutputStream bous = new BufferedOutputStream(conn.getOutputStream());
            bous.write(parameterString.getBytes());
            bous.flush();
            bous.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            if (conn.getResponseCode() == 200) {
                return sb.toString();
            }
            return "카카오 로그인 요청 처리 실패";
        } catch (IOException e) {
            throw new IllegalArgumentException("알 수 없는 카카오 로그인 Access Token 요청 URL 입니다 :: " + KAKAO_SNS_TOKEN_BASE_URL);
        }
    }
}
