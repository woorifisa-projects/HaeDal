package com.haedal.backend.auth.controller;

import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserIdCheckRequest;
import com.haedal.backend.auth.dto.request.UserLoginRequest;
import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.dto.response.UserLoginResponse;
import com.haedal.backend.auth.dto.response.UserRegisterResponse;
import com.haedal.backend.auth.exception.InactiveUserException;
import com.haedal.backend.auth.exception.InvalidIdException;
import com.haedal.backend.auth.exception.InvalidPasswordException;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.model.LogType;
import com.haedal.backend.log.service.LogService;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.dto.response.ProfileResponse;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.service.SubscribeService;
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


@Slf4j
@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;
    private final SubscribeService subscribeService;
    private final LogService logService;

    @GetMapping("/idcheck")
    public ResponseEntity<String> idCheckExists(@RequestParam("id")String id){
        User user = userService.findbyId(id);

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


            String token = userService.login(userLoginRequest.getId(), userLoginRequest.getPassword());
            String userName = userService.findbyId(userLoginRequest.getId()).getName();

            return new ResponseEntity<>(new UserLoginResponse(token, userName), HttpStatus.OK);

    }


    @Transactional
    @PatchMapping("/leave")
    public ResponseEntity<String> deleteUser(Authentication authentication){
        String id = authentication.getName();
//        String id  = userService.getUserId(authentication);
        User user = userService.findbyId(id);
//        user.updateUserStatus(false);
        userService.updateUserStatus(user);
        //구독삭제도 추가하기
        subscribeService.deleteByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(id + " 휴면계정되었습니다");
    }
}