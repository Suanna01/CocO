package com.example.CocO.dto.request;

import com.example.CocO.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomJoinRequest {
    private String password;
}