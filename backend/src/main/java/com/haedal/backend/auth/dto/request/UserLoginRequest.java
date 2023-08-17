package com.haedal.backend.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginRequest {
    private String id;
    private String password;
}