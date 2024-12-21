package com.example.CocO.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;
import java.sql.Timestamp;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // AUTO_INCREMENT 적용

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy; // `Users` 테이블의 `id`를 참조하는 외래 키

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomMember> members; // ChatRoom에 속한 멤버들

    @PrePersist
    public void prePersist() {
        // 엔티티가 처음 저장될 때 createdAt과 updatedAt을 현재 시간으로 설정
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        // 엔티티가 수정될 때 updatedAt을 현재 시간으로 설정
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
