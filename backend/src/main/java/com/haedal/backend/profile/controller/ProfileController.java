package com.haedal.backend.profile.controller;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserUpdateRequest;
import com.haedal.backend.auth.dto.response.UserUpdateResponse;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.profile.dto.response.ProfileResponse;
import com.haedal.backend.profile.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.beans.Transient;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

//    @GetMapping("/{userId}/profile")
//    public ResponseEntity<ProfileResponse> showUserProfile(@PathVariable Long userId){
//        System.out.println(userId + "의 아이디 값을 조회합니다");
//        User user = profileService.findById(userId);
//        return new ResponseEntity<>(ProfileResponse.userNameInfoFrom(user), HttpStatus.OK);
//    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getUserProfile(Authentication authentication){
        String id = authentication.getName();
        User user = profileService.findById(id);
        return new ResponseEntity<>(ProfileResponse.profileInfoFrom(user), HttpStatus.OK);
    }

    @GetMapping("/profile/edit")
    public ResponseEntity<ProfileResponse> showUserProfile(Authentication authentication){
        String id = authentication.getName();
        User user = profileService.findById(id);
        return new ResponseEntity<>(ProfileResponse.allUserInfoFrom(user), HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/profile/edit/save")
    public ResponseEntity<ProfileResponse> UpdateUserProfile(Authentication authentication, @RequestBody UserUpdateRequest userUpdateRequest){
        log.info("요청들어왔다");
        String id = authentication.getName();
        User user = profileService.findById(id);
        user.updateProfile(userUpdateRequest);
        profileService.save(user);
        return new ResponseEntity<>(ProfileResponse.userProfileUpdateFrom(user),HttpStatus.OK);
    }



}