package com.haedal.backend.auth.controller;

import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserIdCheckRequest;
import com.haedal.backend.auth.dto.request.UserLoginRequest;
import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.dto.response.UserLoginResponse;
import com.haedal.backend.auth.dto.response.UserRegisterResponse;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.model.LogType;
import com.haedal.backend.log.service.LogService;
import com.haedal.backend.profile.dto.response.ProfileResponse;
import com.haedal.backend.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;
    private final LogService logService;

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

            User user = profileService.findById(userDto.getUserId());
            LogType logType = LogType.valueOf("SIGNUP");
            LocalDateTime logDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9);
            String logRegister = user.getName()+" 고객님 회원가입";

            Log savelog = logService.save( new Log(user, logType, logDateTime,logRegister));

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

            User user= profileService.findById(userLoginRequest.getId());

            LogType logType = LogType.valueOf("LOGIN");
            LocalDateTime logDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9);
            String logLogIn = user.getName()+" 고객님 로그인";
            System.out.println(logLogIn);
            Log savelog = logService.save( new Log(user, logType, logDateTime,logLogIn));

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