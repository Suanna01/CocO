package com.example.CocO.repository.chat;

import com.example.CocO.entity.ChatRoom;
import com.example.CocO.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findByChatRoom(ChatRoom chatRoom);
}