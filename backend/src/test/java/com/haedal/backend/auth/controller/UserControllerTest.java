package com.haedal.backend.auth.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.dto.response.UserRegisterResponse;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.common.ControllerTest;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

class UserControllerTest extends ControllerTest {
    @DisplayName("회원가입 - 회원가입완료")
    @Test
    void signUp() throws Exception {
        // given
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest("qwert"
                , "asdfasdf"
                , "테스트용이름"
                , "01000000000"
                , UserAgeGroup.ONE
                , ServicePurpose.FORCAR
                , "1234"
                , 10000L
                , 1234);
        given(userService.register(any(UserRegisterRequest.class))).willReturn(UserDto.fromEntity(new User()));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

}