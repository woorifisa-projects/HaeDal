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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.nio.charset.StandardCharsets;



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


//    @DisplayName("회원탈퇴 - 휴면계정으로 변환")
//    @Test
//    @WithMockUser(username = "testUserId",roles = "USER")
//    public void testDeleteUser() throws Exception {
//        // 사용자 ID와 User 객체 생성
//
//        String id = "testUserId";
//        User user = new User(1L, "testUserId");
//
//        // userService.findbyId() 메서드가 호출될 때 예상 결과 설정
//        when(userService.findbyId(id)).thenReturn(user);
//
//        // 로그인 유저 설정(withMockUser로 대체) 로그인 후 토큰 발급 처리를 미리 설정해주어야할듯
//
//        // MockMvc를 사용하여 컨트롤러 엔드포인트 호출
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getName()).thenReturn(id);
//
//        // SecurityContextHolder에 설정
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
//
//        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch("/user/leave")
//                .with(csrf())
//                .with(authentication(authentication))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        // 테스트 결과 검증
//        result.andExpect(status().isOk())
//                .andExpect(content().string(id + "휴면계정되었습니다"));
//
//        // userService.findbyId()가 한 번 호출되었는지 확인
//        verify(userService, times(1)).findbyId(id);
//
//        // user.updateUserStatus()와 subscribeService.deleteByUser()가 각각 한 번 호출되었는지 확인
//        verify(user, times(1)).updateUserStatus(false);
//        verify(subscribeService, times(1)).deleteByUser(user);
//    }
}


