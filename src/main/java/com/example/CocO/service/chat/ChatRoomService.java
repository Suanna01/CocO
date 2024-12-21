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
import java.util.List;
import java.util.stream.Collectors;

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

    // 로그인한 사용자가 생성한 채팅방 목록 조회
    public List<ChatRoomResponse> getMyChatRooms(User user) {
        // 로그인한 사용자가 생성한 채팅방만 가져오기
        List<ChatRoom> chatRooms = chatRoomRepository.findByCreatedBy(user);

        // ChatRoom -> ChatRoomResponse 변환
        return chatRooms.stream()
                .map(chatRoom -> new ChatRoomResponse(chatRoom.getId(), chatRoom.getName(), user.getName(),chatRoom.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // 로그인한 사용자가 참여한 채팅방 목록 조회
    public List<ChatRoomResponse> getChatRooms(User user) {
        // 사용자가 참여한 채팅방 멤버들 조회
        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findByUser(user);

        // 채팅방 리스트 추출
        return chatRoomMembers.stream()
                .map(chatRoomMember -> new ChatRoomResponse(
                        chatRoomMember.getChatRoom().getId(),
                        chatRoomMember.getChatRoom().getName(),
                        chatRoomMember.getChatRoom().getCreatedBy().getName(),
                        chatRoomMember.getChatRoom().getCreatedAt()))
                .collect(Collectors.toList());
    }
}
