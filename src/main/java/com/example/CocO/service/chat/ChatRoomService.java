package com.example.CocO.service.chat;

import com.example.CocO.dto.request.ChatRoomCreateRequest;
import com.example.CocO.dto.request.ChatRoomJoinRequest;
import com.example.CocO.dto.response.ChatRoomResponse;
import com.example.CocO.entity.ChatRoom;
import com.example.CocO.entity.ChatRoomMember;
import com.example.CocO.entity.User;
import com.example.CocO.repository.chat.ChatRoomMemberRepository;
import com.example.CocO.repository.chat.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomCreateRequest request, User currentUser) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(request.getName())
                .password(request.getPassword())
                .createdBy(currentUser)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return new ChatRoomResponse(
                savedChatRoom.getId(),
                savedChatRoom.getName(),
                savedChatRoom.getCreatedBy().getName(),
                savedChatRoom.getCreatedAt()
        );
    }

    @Transactional
    public boolean joinChatRoom(Long chatRoomId, ChatRoomJoinRequest request, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        if (!chatRoom.getPassword().equals(request.getPassword())) {
            return false; // 비밀번호가 틀릴 경우 참여 불가
        }

        ChatRoomMember member = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .user(user)
                .joinedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        chatRoomMemberRepository.save(member);
        return true;
    }
}
