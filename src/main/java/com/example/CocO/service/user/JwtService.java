package com.example.CocO.service.user;

import com.example.CocO.entity.User;
import com.example.CocO.repository.user.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String JWT_SECRET_KEY;  // JWT 서명에 사용할 비밀 키

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(User user) {
        // JWT 생성 로직 (예시)
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))  // 사용자 정보 (예: 이름, 이메일 등)
                .claim("id", user.getId())
                .setIssuedAt(new Date())  // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 만료 시간 (1시간)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)  // 서명
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UserDetails loadUserById(Long userId) {
        // UserRepository를 사용하여 User를 조회하고 UserDetails로 반환
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}