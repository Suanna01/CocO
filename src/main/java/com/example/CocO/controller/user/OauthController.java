package com.example.CocO.controller.user;

import com.example.CocO.dto.response.UserResponse;
import com.example.CocO.entity.User;
import com.example.CocO.helper.constants.SocialLoginType;
import com.example.CocO.service.user.JwtService;
import com.example.CocO.service.user.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
@Tag(name = "OAuth", description = "소셜 로그인 인증을 시작하고 콜백을 처리하는 API를 제공합니다.")
public class OauthController {

    private final OauthService oauthService;
    private final JwtService jwtService;

    @Operation(
            summary = "소셜 로그인 프로세스 시작",
            description = "이 API는 사용자를 소셜 로그인 페이지로 리다이렉트하여 인증 절차를 시작합니다.",
            operationId = "startSocialLogin",
            parameters = {
                    @Parameter(name = "socialLoginType", description = "소셜 로그인 유형 (예: Kakao, Google 등).", required = true)
            })
    @GetMapping(value = "/{socialLoginType}")
    public ResponseEntity<String> socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        String redirectURL = oauthService.request(socialLoginType);
        return ResponseEntity.ok(redirectURL);  // 리다이렉션 URL을 응답으로 반환
    }

    @Operation(
            summary = "소셜 로그인 콜백 처리 (백엔드에서 사용 X)",
            description = "사용자가 소셜 로그인 후 콜백 URL로 받은 코드를 통해 액세스 토큰을 요청합니다.",
            operationId = "handleSocialLoginCallback",
            parameters = {
                    @Parameter(name = "socialLoginType", description = "소셜 로그인 유형 (예: Kakao, Google 등).", required = true),
                    @Parameter(name = "code", description = "소셜 로그인 API 서버로부터 받은 인증 코드.", required = true)
            })
    @GetMapping(value = "/{socialLoginType}/callback")
    public ResponseEntity<?> callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code,
            HttpSession session) {

        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);

        // 액세스 토큰을 통해 사용자 정보를 받아온 후 저장
        User user = oauthService.requestAccessTokenAndSaveUser(socialLoginType, code);

        if (user != null) {
            log.info(">> 사용자 정보 DB 저장 완료 :: {}", user.getName());

            // JWT 토큰 생성
            String jwtToken = jwtService.generateToken(user);

            // 세션에 사용자 정보 저장
            session.setAttribute("loginUser", user);

            // 로그인한 유저 정보를 response body로 반환
            return ResponseEntity.ok(new UserResponse(user.getName(), user.getProvider(), user.getAccessToken(), jwtToken));
        } else {
            log.error(">> 사용자 정보 저장 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 정보 저장 실패");
        }
    }

}
