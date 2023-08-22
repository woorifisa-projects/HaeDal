package com.haedal.backend.auth.controller;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserLoginRequest;
import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.dto.response.UserDataResponse;
import com.haedal.backend.auth.dto.response.UserLoginResponse;
import com.haedal.backend.auth.dto.response.UserRegisterResponse;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.profile.dto.response.ProfileResponse;
import com.haedal.backend.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;



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

        return new ResponseEntity<>(ProfileResponse.profileInfoFrom(user), HttpStatus.OK);
    }


}