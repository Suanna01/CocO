package com.example.CocO.entity;

import lombok.*;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // AUTO_INCREMENT 적용

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom; // `ChatRooms` 테이블의 `id`를 참조하는 외래 키

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // `Users` 테이블의 `id`를 참조하는 외래 키

    @Column(name = "content", nullable = false)
    private String content; // 메시지 내용

    @Column(name = "sent_at")
    private Timestamp sentAt; // 메시지 전송 시각
}
