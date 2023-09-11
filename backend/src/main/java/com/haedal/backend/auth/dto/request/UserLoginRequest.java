package com.haedal.backend.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor //-1,3이싱
@Getter
@AllArgsConstructor
@Builder
public class UserLoginRequest {
    private String id;
    private String password;
}