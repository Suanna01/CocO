package com.example.CocO.controller.chat;

import com.example.CocO.dto.request.ChatRoomCreateRequest;
import com.example.CocO.dto.request.ChatRoomJoinRequest;
import com.example.CocO.dto.response.ChatRoomResponse;
import com.example.CocO.entity.ChatRoom;
import com.example.CocO.entity.User;
import com.example.CocO.repository.user.UserRepository;
import com.example.CocO.service.chat.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    // 채팅방 생성
    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<ChatRoomResponse> createChatRoom(
            @RequestBody ChatRoomCreateRequest request,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("채팅방 이름은 필수입니다.");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        ChatRoomResponse chatRoomResponse = chatRoomService.createChatRoom(request, user);

        if (chatRoomResponse == null) {
            throw new IllegalStateException("채팅방 생성에 실패했습니다.");
        }

        return ResponseEntity.ok(chatRoomResponse);
    }


    // 채팅방 참여
    @Operation(summary = "채팅방 참여", description = "주어진 비밀번호로 채팅방에 참여합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "참여 성공"),
            @ApiResponse(responseCode = "401", description = "비밀번호가 틀림")
    })
    @PostMapping("/{chatRoomId}/join")
    public ResponseEntity<String> joinChatRoom(
            @PathVariable Long chatRoomId,
            @RequestBody ChatRoomJoinRequest request,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        boolean success = chatRoomService.joinChatRoom(chatRoomId, request, user);
        if (success) {
            return ResponseEntity.ok("참여 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
        }
    }
}
