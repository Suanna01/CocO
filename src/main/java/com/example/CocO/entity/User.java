package com.example.CocO.entity;

import lombok.*;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // AUTO_INCREMENT 적용

    @Column(name = "social_id", nullable = false, unique = true)
    private String socialId; // 소셜 로그인에서 받은 사용자 ID (예: Google ID, Kakao ID)

    @Column(name = "provider", nullable = false)
    private String provider; // 소셜 로그인 제공자 (Google, Kakao, Naver 등)

    @Column(name = "access_token", nullable = true)
    private String accessToken; // 액세스 토큰 (소셜 로그인 인증 후 받은 토큰)

    @Column(name = "name", nullable = true)
    private String name; // 사용자 이름 (옵션)

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "user")
    private List<ChatRoomMember> chatRoomMembers; // User가 참여한 ChatRooms에 대한 관계

    @OneToMany(mappedBy = "sender")
    private List<Message> messages; // 사용자가 보낸 메시지들에 대한 관계

    @PrePersist
    public void prePersist() {
        // 엔티티가 처음 저장될 때 createdAt을 현재 시간으로 설정
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
