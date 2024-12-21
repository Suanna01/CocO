package com.example.CocO.repository.chat;

import com.example.CocO.entity.ChatRoom;
import com.example.CocO.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // createdBy가 주어진 사용자와 일치하는 채팅방 리스트를 조회
    List<ChatRoom> findByCreatedBy(User user);
}
