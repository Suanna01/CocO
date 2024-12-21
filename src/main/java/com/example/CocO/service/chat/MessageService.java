package com.example.CocO.service.chat;

import com.example.CocO.dto.request.MessageRequest;
import com.example.CocO.entity.ChatRoom;
import com.example.CocO.entity.Message;
import com.example.CocO.entity.User;
import com.example.CocO.repository.chat.ChatRoomMemberRepository;
import com.example.CocO.repository.chat.ChatRoomRepository;
import com.example.CocO.repository.chat.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void sendMessage(MessageRequest request, User user) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(request.getChatRoomId());
        ChatRoom chatRoom = optionalChatRoom.orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        // 메시지 저장
        Message message = Message.builder()
                .chatRoom(chatRoom)
                .sender(user)
                .content(request.getContent())
                .sentAt(new Timestamp(System.currentTimeMillis()))
                .build();
        messageRepository.save(message);

        if (chatRoom.getCreatedBy().getId().equals(user.getId())) {
            System.out.println("나(교수: " + user.getProvider() + user.getName() + ") -> ");
        } else {
            System.out.println("나(학생: " + user.getProvider() + user.getName() + ") -> ");
        }

        // 메시지 전송 로직
        if (chatRoom.getCreatedBy().getId().equals(user.getId())) {
            // 1쪽 User -> 모든 N쪽 User로 메시지 브로드캐스팅
            chatRoomMemberRepository.findAllByChatRoom(chatRoom).forEach(member -> {
                System.out.println("\t 학생에게 전송 (학생: " +
                        member.getUser().getProvider() +
                        member.getUser().getName() + ")");
                // 메시지 전송
                messagingTemplate.convertAndSendToUser(
                        member.getUser().getSocialId(),
                        "/queue/messages",
                        message.getContent()
                );
            });
        } else {
            // N쪽 User -> 1쪽 User로 메시지 전송
            System.out.println("\t 교수에게 전송 (교수: " +
                    chatRoom.getCreatedBy().getProvider() +
                    chatRoom.getCreatedBy().getName() + ")");
            // 메시지 전송
            messagingTemplate.convertAndSendToUser(
                    chatRoom.getCreatedBy().getSocialId(),
                    "/queue/messages",
                    message.getContent()
            );
        }
    }
}
