package com.haedal.backend.auth.controller;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserLoginRequest;
import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.dto.response.UserLoginResponse;
import com.haedal.backend.auth.dto.response.UserRegisterResponse;
import com.haedal.backend.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        try {
            UserDto userDto = userService.register(userRegisterRequest);
            return new ResponseEntity<>(new UserRegisterResponse(userDto.getId()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        try {
            String token = userService.login(userLoginRequest.getId(), userLoginRequest.getPassword());
            return new ResponseEntity<>(new UserLoginResponse(token), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}