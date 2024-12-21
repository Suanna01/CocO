package com.example.CocO.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    private Long chatRoomId;
    private String content;
}
