package com.haedal.backend.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResponse {
    private String token;
}