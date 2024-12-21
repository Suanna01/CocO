package com.example.CocO.controller.chat;

import com.example.CocO.dto.request.MessageRequest;
import com.example.CocO.entity.User;
import com.example.CocO.service.chat.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "채팅", description = "채팅 API를 제공합니다.")
public class MessageController {

    private final MessageService messageService;

    // 메시지 전송
    @Operation(summary = "메시지 전송", description = "특정 채팅방에 메시지를 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메시지 전송 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<String> sendMessage(
            @RequestBody MessageRequest request,
            @AuthenticationPrincipal User user) {
        messageService.sendMessage(request, user);
        return ResponseEntity.ok("메시지 전송 완료");
    }

}
