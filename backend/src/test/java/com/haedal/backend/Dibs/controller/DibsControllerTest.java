package com.haedal.backend.Dibs.controller;
import com.haedal.backend.common.ControllerTest;
import com.haedal.backend.log.model.LogType;
import org.mockito.InjectMocks;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.Dibs.service.DibsService;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.service.LogService;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import com.haedal.backend.profile.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any; // 추가해야 하는 import 문
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DibsControllerTest extends ControllerTest {
    @InjectMocks
    private DibsController dibsController;

    private User user;
    private Product product;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .userId(1L)
                .id("testuser")
                .authNumber(12345)
                .asset(1000000L)
                .build();

        product = Product.builder()
                .productId(1L)
                .requiredStartMoney(500)
                .maxProductMoney(10000L)
                .servicePurpose(ServicePurpose.MOKDON)
                .userAgeGroup(UserAgeGroup.THREE)
                .tag(Tag.FINANCE)
                .productName("첫거래 우대 정기예금")
                .shortInfo("shortInfo 1")
                .longInfo("longInfo 1")
                .period(12)
                .interestRate(1.3)
                .isDeposit(true)
                .productStatus(true)
                .build();

    }

    @DisplayName("찜 추가")
    @Test
    @WithMockUser
    void addDibs() throws Exception {

        Long productId = 1L;

        // Mock 객체 생성
        String username = "testuser";
        Authentication authentication = new TestingAuthenticationToken(username, null, "USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LogType logType = LogType.DIB;
        LocalDateTime logDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9);
        String logDib = "id" + product.getProductId() + " " + product.getProductName() + " 찜";

        System.out.println(productService);
        System.out.println(logService);
        System.out.println(user.getUserId());
        System.out.println(product.getProductId());

        // productService Mock 객체 생성 및 설정
        when(productService.findByProductId(productId))
                .thenReturn(product); // productId에 해당하는 상품 객체를 반환하도록 설정

        // logService Mock 객체 생성
        Log mockLog = Log.builder()
                .user(user)
                .logTime(logDateTime)
                .logDesc(logDib)
                .build();

        // logService의 save 메서드가 호출될 때 mockLog를 반환하도록 설정
        when(logService.save(any(Log.class))).thenReturn(mockLog);

        List<Dibs> dibs = null;

        // dibsService Mock 객체 생성
        when(dibsService.findDibsByUserIDProductID(any(), any())).thenReturn(any());

        // POST /dibs/{productId}/add 요청 테스트
        mockMvc.perform(MockMvcRequestBuilders.post("/dibs/1/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(authentication(authentication)))
                .andExpect(status().isOk());
    }


    @DisplayName("찜 중복")
    @Test
    void testAddDibsDuplicate() throws Exception {
        // Mock 객체 생성
        String username = "testuser";
        Authentication authentication = new TestingAuthenticationToken(username, null, "USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        long userId = 1L;
        long productId = 1L;

        given(profileService.getUserId(user)).willReturn(1L);

        System.out.println(user.getUserId());
        System.out.println(product.getProductId());

        // 가짜 찜 목록 생성 (찜이 이미 존재하는 상황)
        List<Dibs> existingDibsList = new ArrayList<>();
        existingDibsList.add(new Dibs(user, product, LocalDate.now()));
        System.out.println(existingDibsList);
        System.out.println("경계썬");

        // findDibsByUserIDProductID 메서드 호출에 대한 가짜 반환값 설정
        given(dibsService.findDibsByUserIDProductID(any(), any())).willReturn(existingDibsList);


        // POST /dibs/{productId}/add 요청 테스트 (찜 중복)
        mockMvc.perform(MockMvcRequestBuilders.post("/dibs/1/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(authentication(authentication)))
                    .andExpect(status().isOk());
        // dibsService.save가 호출되지 않았는지 검증
        verify(dibsService, never()).save(any(Dibs.class));
    }


    @DisplayName("찜 취소")
    @Test
    @WithMockUser
    void deleteDibs() throws Exception {
        // Mock 객체 생성
        String username = "testuser";
        Authentication authentication = new TestingAuthenticationToken(user.getName(), null, "USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Dibs 객체 생성 및 설정
        List<Dibs> existingDibsList = new ArrayList<>();
        existingDibsList.add(new Dibs(user, product, LocalDate.now()));
        System.out.println(existingDibsList);

        given(userService.getUserId(authentication)).willReturn(username);
        given(dibsService.findDibsByUserIDProductID(user.getUserId(), product.getProductId())).willReturn(existingDibsList);
        when(logService.save(any(Log.class))).thenReturn(new Log());

        // DELETE /dibs/{productId}/delete 요청 테스트
        mockMvc.perform(MockMvcRequestBuilders.delete("/dibs/1/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("찜이 취소되었습니다."));

        // logService.save가 한 번 호출되었는지 검증
        verify(logService, times(1)).save(any(Log.class));
    }

    @DisplayName("찜 여부 확인")
    @Test
    void checkDibs() throws Exception {
        // Mock 객체 생성
        Authentication authentication = new TestingAuthenticationToken(user.getName(), null, "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 첫 번째 테스트 케이스: 찜한 상품을 확인
        // Dibs 객체를 생성하지 않고, findDibsByUserIDProductID 메서드가 비어있지 않은 리스트를 반환하도록 모의 설정
        given(dibsService.findDibsByUserIDProductID(user.getUserId(), product.getProductId()))
                .willReturn(Collections.singletonList(new Dibs(user, product, LocalDate.now())));

        // GET /dibs/{productId}/check 요청 테스트 (찜한 상품)
        mockMvc.perform(MockMvcRequestBuilders.get("/dibs/1/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));

    }
}