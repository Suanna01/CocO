package com.example.CocO.repository.user;

import com.example.CocO.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 소셜 로그인 ID로 사용자 찾기
    Optional<User> findBySocialId(String socialId);
}