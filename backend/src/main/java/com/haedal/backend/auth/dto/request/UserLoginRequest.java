package com.haedal.backend.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor //-1,3이싱
@Getter
public class UserLoginRequest {
    private String id;
    private String password;
}