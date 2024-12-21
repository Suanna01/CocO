package com.example.CocO.dto.request;

import com.example.CocO.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomCreateRequest {
    private String name;
    private String password;
}
