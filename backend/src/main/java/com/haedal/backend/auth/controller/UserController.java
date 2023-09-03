package com.haedal.backend.auth.controller;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserIdCheckRequest;
import com.haedal.backend.auth.dto.request.UserLoginRequest;
import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.dto.response.UserLoginResponse;
import com.haedal.backend.auth.dto.response.UserRegisterResponse;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.profile.dto.response.ProfileResponse;
import com.haedal.backend.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;

    @GetMapping("/idcheck")
    public ResponseEntity<String> idCheckExists(@RequestParam("id")String id){
        User user = profileService.findById(id);

        if (user != null) {
            // ID가 이미 존재하는 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ID already exists");
        } else {
            // ID가 존재하지 않는 경우
            return ResponseEntity.ok("ID available");
        }
    }


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

    @GetMapping("/alog")
    public ResponseEntity<ProfileResponse> getUserId(Authentication authentication){
        String id = authentication.getName();
        User user = profileService.findById(id);

        return new ResponseEntity<>(ProfileResponse.userNameInfoFrom(user), HttpStatus.OK);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<String> deleteUser(Authentication authentication){
        String id = authentication.getName();
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(id + "회원탈퇴완료");
    }
}