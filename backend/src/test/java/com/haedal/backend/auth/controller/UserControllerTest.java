package com.haedal.backend.auth.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserIdCheckRequest;
import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.common.ControllerTest;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import com.haedal.backend.subscribe.model.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;


class UserControllerTest extends ControllerTest {

    @InjectMocks
    private UserController userController;

    @DisplayName("아이디 중복체크 - 아이디 존재할 경우")
    @Test
    @WithMockUser
    public void testIdCheckExists() throws Exception {
        //given
        String existingId = "existingUserId"; // 이미 존재하는 ID
        String nonExistingId = "nonExistingUserId"; // 존재하지 않는 ID

        User existuser = new User(
                1L,
                "nonExistingUserId"
        );

        User nonexistuser = null;

        given(userService.findbyId(existingId)).willReturn(existuser);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/idcheck")
                        .param("id", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("ID already exists"))
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("idCheckExists",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));

//        given(userService.findbyId(nonExistingId)).willReturn(nonexistuser);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/idcheck")
//                        .param("id", nonExistingId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("ID available"))
//                .andDo(print())
//                .andDo(MockMvcRestDocumentation.document("idCheckExists",
//                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
//                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));

    }



    @DisplayName("아이디 중복체크 - 아이디 존재하지않을경우")
    @Test
    @WithMockUser
    public void testIdCheckExists2() throws Exception {
        //given

        String nonExistingId = "nonExistingUserId"; // 존재하지 않는 ID

        User nonexistuser = null;

        given(userService.findbyId(nonExistingId)).willReturn(nonexistuser);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/idcheck")
                        .param("id", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ID available"))
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("idCheckExists",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));

    }




    @DisplayName("회원가입 - 회원가입완료")
    @Test
    @WithMockUser
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

        UserDto userDto = new UserDto(
                1L,
                "asdfasdf"
                , "asdfasdf"
                , "테스트용이름"
                , "01000000000"
                , UserAgeGroup.ONE
                , ServicePurpose.FORCAR
                , "1234"
                , 10000L
                , 1234
        );
        given(userService.register(any(UserRegisterRequest.class))).willReturn(userDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterRequest)))
                .andDo(print())
                .andDo(document("user/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andDo(document("user/register",
                        responseFields(
                                fieldWithPath("id").description("회원가입완료되었습니다")
                        )))
                .andReturn();
    }


    @DisplayName("회원탈퇴 - 휴면계정으로 변환")
    @Test

    public void testDeleteUser() throws Exception {
        // 가상 사용자 정보 생성
        String username = "testuser";
        Authentication authentication =new TestingAuthenticationToken(username, null,"ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // userService.getUserId() 메서드의 Mock 설정
        given(userService.getUserId(authentication)).willReturn(username);

        // userService.findbyId() 메서드의 Mock 설정
//        User user = new User(1L,username);
//        List<Subscribe> subscribeList =;

        User user = User.builder().userId(1L).id(username).password("1234").userStatus(true).build();
        given(userService.findbyId(username)).willReturn(user);

        // 컨트롤러 호출
        mockMvc.perform(MockMvcRequestBuilders.patch("/user/leave")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(username + " 휴면계정되었습니다"));

        // userService.updateUserStatus() 및 subscribeService.deleteByUser() 메서드 호출 여부 검증
        verify(userService, times(1)).updateUserStatus(user);
        verify(subscribeService, times(1)).deleteByUser(user);
    }

}










