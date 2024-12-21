package com.example.CocO.repository.chat;

import com.example.CocO.entity.ChatRoom;
import com.example.CocO.entity.ChatRoomMember;
import com.example.CocO.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    // 로그인한 사용자가 참여한 채팅방 목록 조회
    List<ChatRoomMember> findByUser(User user);

    // 주어진 채팅방에 속한 모든 멤버를 조회
    List<ChatRoomMember> findAllByChatRoom(ChatRoom chatRoom);

}