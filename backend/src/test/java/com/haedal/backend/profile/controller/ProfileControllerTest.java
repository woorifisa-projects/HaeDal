package com.haedal.backend.profile.controller;

import static org.mockito.BDDMockito.*;
import com.haedal.backend.auth.dto.request.UserPasswordCheckRequest;
import com.haedal.backend.auth.dto.request.UserUpdateRequest;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.common.ControllerTest;
import com.haedal.backend.profile.dto.response.ProfileResponse;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest extends ControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("보안설정에서 비밀번호를 입력하면 비밀번호 체크를 진행한다.")
    @Test
    public void testPasswordDB() throws Exception{

        //given - authentication
        String id = "testuser";
        String password = "testpassword";
        Authentication authentication =new TestingAuthenticationToken(id, null,"ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //given - UserPasswordCheckRequest 설정
        UserPasswordCheckRequest userPasswordCheckRequest = new UserPasswordCheckRequest(password);
        //given - User설정
        User user = User.builder().userId(1L).id(id).name("테스트용계정").password(password).build();
        //given - 반환객체설정
        ProfileResponse profileResponse = new ProfileResponse(user.getPassword(),user.getId());

        given(profileService.findById(id)).willReturn(user);
        given(profileService.getUserPasswordById(id)).willReturn(password);
        given(bCryptPasswordEncoder.matches(password, user.getPassword())).willReturn(true);
        // 컨트롤러 호출
        mockMvc.perform(MockMvcRequestBuilders.post("/security")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPasswordCheckRequest))
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication)))
                .andDo(print())
                .andExpect(status().isOk()) // 상태 코드가 200 OK인지 확인합니다.
                .andExpect(jsonPath("$.password").value(profileResponse.userCheckPwFrom(user).getPassword()))
                .andExpect(jsonPath("$.name").value(profileResponse.userCheckPwFrom(user).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("testpassword"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("테스트용계정"));

        //.andExpect(MockMvcResultMatchers.jsonPath("$.productName").value("첫거래 우대 정기예금"))
        //                .andExpect(MockMvcResultMatchers.jsonPath("$.period").value(12));
    }

    @DisplayName("비밀번호변경을 진행하면 비밀번호가 저장된다.")
    @Test
    public void changePassword() throws Exception{

        String id = "testuser";
        String password = "testpassword";
        Authentication authentication =new TestingAuthenticationToken(id, null,"ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String hashPassword="hashtestpassword";

        User user = User.builder().userId(1L).id(id).password(password).build();
        User updateUser = User.builder().userId(1L).id(id).password(hashPassword).build();
        ProfileResponse profileResponse = new ProfileResponse(hashPassword,id);

        //given - UserPasswordCheckRequest 설정
        UserPasswordCheckRequest userPasswordCheckRequest = new UserPasswordCheckRequest(password);
        given(bCryptPasswordEncoder.encode(userPasswordCheckRequest.getPassword())).willReturn(hashPassword);
        given(profileService.findById(id)).willReturn(user);
        given(profileService.updatePassword(user,hashPassword)).willReturn(updateUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/changePW")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPasswordCheckRequest))
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication)))
                .andDo(print())
                .andExpect(status().isOk()) // 상태 코드가 200 OK인지 확인합니다.
                .andExpect(jsonPath("$.password").value(profileResponse.getPassword()))
                .andExpect(jsonPath("$.id").value(profileResponse.getId()));
    }

    @DisplayName("내 프로필에 접속하면 해당 유저의 프로필정보(이름, 전화번호, 이용목적, 연령대) 를 반환한다")
    @Test
    public void getUserProfile() throws  Exception{

        //given authentication 설정
        String id = "testuser";
        Authentication authentication =new TestingAuthenticationToken(id, null,"ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // User 객체 설정
        User user = User.builder().id(id).password("testpassword").name("테스트용계정").phoneNumber("01012345678").servicePurpose(ServicePurpose.MOKDON).userAgeGroup(UserAgeGroup.ONE).accountNumber("12345678").asset(100000000L).authNumber(1234).build();
        ProfileResponse profileResponse = new ProfileResponse(user.getId(),user.getPassword(),user.getName(),user.getPhoneNumber(),user.getServicePurpose(),user.getUserAgeGroup(),user.getAccountNumber(),user.getAsset(),user.getAuthNumber());
        given(profileService.findById(id)).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication)))
                .andDo(print())
                .andExpect(status().isOk()) // 상태 코드가 200 OK인지 확인합니다.
                .andExpect(jsonPath("$.name").value(profileResponse.profileInfoFrom(user).getName()))
                .andExpect(jsonPath("$.phoneNumber").value(profileResponse.profileInfoFrom(user).getPhoneNumber()))
                .andExpect(jsonPath("$.servicePurpose").value(profileResponse.profileInfoFrom(user).getServicePurpose().name()))
                .andExpect(jsonPath("$.userAgeGroup").value(profileResponse.profileInfoFrom(user).getUserAgeGroup().name()));

    }

    @DisplayName("프로필 수정하기 버튼을 누르면 해당 유저의 전체 프로필 정보를 받아온다")
    @Test
    public void getAllProfileUser() throws Exception{
        //given authentication 설정
        String id = "testuser";
        Authentication authentication =new TestingAuthenticationToken(id, null,"ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // User 객체 설정
        User user = User.builder().id(id).password("testpassword").name("테스트용계정").phoneNumber("01012345678").servicePurpose(ServicePurpose.MOKDON).userAgeGroup(UserAgeGroup.ONE).accountNumber("12345678").asset(100000000L).authNumber(1234).build();
        ProfileResponse profileResponse = new ProfileResponse(user.getId(),user.getPassword(),user.getName(),user.getPhoneNumber(),user.getServicePurpose(),user.getUserAgeGroup(),user.getAccountNumber(),user.getAsset(),user.getAuthNumber());
        given(profileService.findById(id)).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/edit")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication)))
                .andDo(print())
                .andExpect(status().isOk()) // 상태 코드가 200 OK인지 확인합니다.
                .andExpect(jsonPath("$.name").value(profileResponse.allUserInfoFrom(user).getName()))
                .andExpect(jsonPath("$.phoneNumber").value(profileResponse.allUserInfoFrom(user).getPhoneNumber()))
                .andExpect(jsonPath("$.servicePurpose").value(profileResponse.allUserInfoFrom(user).getServicePurpose().name()))
                .andExpect(jsonPath("$.userAgeGroup").value(profileResponse.allUserInfoFrom(user).getUserAgeGroup().name()))
                .andExpect(jsonPath("$.accountNumber").value(profileResponse.allUserInfoFrom(user).getAccountNumber()))
                .andExpect(jsonPath("$.asset").value(profileResponse.allUserInfoFrom(user).getAsset()))
                .andExpect(jsonPath("$.authNumber").value(profileResponse.allUserInfoFrom(user).getAuthNumber()));

    }

    @DisplayName("프로필 정보 변경 후 바뀐 정보가 업데이트된다.")
    @Test
    public void editProfileUser() throws Exception{
        //given authentication 설정
        String id = "testuser";
        Authentication authentication =new TestingAuthenticationToken(id, null,"ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // User 객체 설정
        User user = User.builder().id(id).password("testpassword").name("테스트용계정").phoneNumber("01012345678").servicePurpose(ServicePurpose.MOKDON).userAgeGroup(UserAgeGroup.ONE).accountNumber("12345678").asset(100000000L).authNumber(1234).build();
        User updateUser = User.builder().id(id).password("testpassword").name("테스트용계정2").phoneNumber(user.getPhoneNumber()).servicePurpose(ServicePurpose.FORCAR).userAgeGroup(UserAgeGroup.THREE).accountNumber(user.getAccountNumber()).asset(100000000L).authNumber(5678).build();
        // Request 객체 설정
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().name("테스트용계정2").phoneNumber("01012345678").servicePurpose(ServicePurpose.FORCAR).userAgeGroup(UserAgeGroup.THREE).accountNumber("12345678").asset(100000000L).authNumber(5678).build();
        ProfileResponse profileResponse = new ProfileResponse(updateUser.getId(),updateUser.getPassword(),updateUser.getName(),updateUser.getPhoneNumber(),updateUser.getServicePurpose(),updateUser.getUserAgeGroup(),updateUser.getAccountNumber(),updateUser.getAsset(),updateUser.getAuthNumber());

        given(profileService.findById(id)).willReturn(user);
        given(profileService.userUpdateProfile(user,userUpdateRequest)).willReturn(profileResponse);


        mockMvc.perform(MockMvcRequestBuilders.patch("/profile/edit/save")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication)))
                .andDo(print())
                .andExpect(status().isOk()) ;// 상태 코드가 200 OK인지 확인합니다.


    }
}