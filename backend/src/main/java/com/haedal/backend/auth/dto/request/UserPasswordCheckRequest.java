package com.haedal.backend.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
//@NoArgsConstructor
@Getter
//@AllArgsConstructor 2이상
@NoArgsConstructor // 무적이고 신이다 - 1일때 이거없으면 에러남
public class UserPasswordCheckRequest {
    private  String password;

    public UserPasswordCheckRequest(String password) {
        this.password = password;
    }
}


