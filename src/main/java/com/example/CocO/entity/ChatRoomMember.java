package com.example.CocO.entity;

import lombok.*;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "chat_room_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // AUTO_INCREMENT 적용

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom; // `ChatRooms` 테이블의 `id`를 참조하는 외래 키

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // `Users` 테이블의 `id`를 참조하는 외래 키

    @Column(name = "joined_at")
    private Timestamp joinedAt;
}
