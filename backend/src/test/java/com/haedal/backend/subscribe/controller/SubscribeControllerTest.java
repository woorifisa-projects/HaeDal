package com.haedal.backend.subscribe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.service.LogService;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.dto.response.PortfolioResponse;
import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.subscribe.service.SubscribeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscribeController.class)
class SubscribeControllerTest {
    @MockBean // SubscribeController가 사용하는 서비스 빈들을 MockBean으로 주입
    private SubscribeService subscribeService;
    @MockBean
    private ProfileService profileService;
    @MockBean
    private ProductService productService;
    @MockBean
    private LogService logService;

    @Autowired
    private MockMvc mockMvc;


    private ObjectMapper objectMapper;
    private User user;
    private Product product;
    private static final MediaType TEXT_PLAIN_UTF8 = new MediaType("text", "plain", Charset.forName("UTF-8"));


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        user =User.builder().userId(1L)
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

        // 모의 객체에 대한 행동 설정
        Mockito.when(profileService.findById(anyString())).thenReturn(user);
        Mockito.when(productService.findByProductId(Mockito.anyLong())).thenReturn(product);
    }

    @DisplayName("해당 상품 상품 정보 표출")
    @Test
    @WithMockUser(username = "testuser")
    void showSubscribeProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/subscribe/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName").value("첫거래 우대 정기예금"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.period").value(12));
    }

    @DisplayName("유저와 상품 정보에 관한 상품 등록 페이지")
    @Test
    @WithMockUser(username = "testuser")
    void showSubscribeProductAndUser() throws Exception {
        when(profileService.findById(user.getUserId())).thenReturn(user);
        when(productService.findByProductId(product.getProductId())).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/subscribe/" + product.getProductId() + "/semi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(product.getProductId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(user.getAccountNumber()));
    }

    //----아래는 모두 상품 신청에 관한 코드------------

    @DisplayName("상품 신청 페이지 '신청'버튼 클릭 - 성공적인 경우")
    @Test
    @WithMockUser
    void testSubscribeProduct_Success() throws Exception {
        // 준비된 데이터
        Map<String, String> requestData = new HashMap<>();
        requestData.put("authenticationNumber", "12345");
        requestData.put("startMoney", "1000");

        // 행동 설정
        Mockito.when(subscribeService.findSubscriptionsByProductsAndUser(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Collections.emptyList()); // 이미 구독한 내역이 없는 것으로 설정
        Mockito.when(logService.save(Mockito.any())).thenReturn(new Log());

        // 요청 및 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/subscribe/"+ product.getProductId() +"/final")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(TEXT_PLAIN_UTF8))
                .andExpect(MockMvcResultMatchers.content().string("신청이 완료되었습니다."));
    }

    @DisplayName("상품 신청 페이지 '신청'버튼 클릭 - 이미 구독한 경우")
    @Test
    @WithMockUser
    void testSubscribeProduct_AlreadySubscribed() throws Exception {
        // 준비된 데이터
        Map<String, String> requestData = new HashMap<>();
        requestData.put("authenticationNumber", "12345");
        requestData.put("startMoney", "1000");

        // 행동 설정
        Mockito.when(subscribeService.findSubscriptionsByProductsAndUser(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(List.of(new Subscribe())); // 이미 구독한 내역이 있는 것으로 설정

        // 요청 및 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/subscribe/1/final")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("이미 해당 상품을 구독하고 있습니다."));
    }

    @DisplayName("상품 신청 페이지 '신청'버튼 클릭 - 올바르지 않은 정보 입력")
    @Test
    @WithMockUser
    void testSubscribeProduct_InvalidInfo() throws Exception {
        // 준비된 데이터
        Map<String, String> requestData = new HashMap<>();
        requestData.put("authenticationNumber", "12345");
        requestData.put("startMoney", "50000"); // 너무 큰 금액

        // 요청 및 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/subscribe/1/final")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("입력하신 정보가 올바르지 않습니다."));
    }

    //----여기까지 상품 구독에 관련된 테스트 코드------------
    @DisplayName("포트폴리오 정보 표출")
    @Test
    @WithMockUser(username = "testuser")
    void getUserSubscribeListOrderByMoney() throws Exception {
        // 가상의 포트폴리오 정보 생성
        List<PortfolioResponse> portfolioList = new ArrayList<>();
        portfolioList.add(PortfolioResponse.builder()
                .subscribeId(1L)
                .presentMoney(1000L)
                .startMoney(500L)
                .subscribeDate(LocalDate.of(2022, 1, 1))
                .productId(1L)
                .userId(1L)
                .interestRate(0.02)
                .isDeposit(true)
                .longInfo("Long info 1")
                .maxProductMoney(10000L)
                .period(12)
                .productName("Product 1")
                .requiredStartMoney(500)
                .servicePurpose(ServicePurpose.FORCAR)
                .shortInfo("Short info 1")
                .tag(Tag.FINANCE)
                .userAgeGroup(UserAgeGroup.THREE)
                .progressdate(200L)
                .endSubscribeDate(LocalDate.of(2023, 1, 1))
                .build());

        // Mock 객체의 행동 설정
        when(profileService.findById(anyString())).thenReturn(new User(1L, "testuser"));
        when(subscribeService.findSubscriptionsAndProductsByUserSortedByMoney(anyString())).thenReturn(portfolioList);

        // API 요청 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/subscribe/portfolio")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].subscribeId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].presentMoney").value(1000L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].startMoney").value(500L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].interestRate").value(0.02))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].deposit").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].longInfo").value("Long info 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].maxProductMoney").value(10000L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].period").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productName").value("Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requiredStartMoney").value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].servicePurpose").value("FORCAR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].shortInfo").value("Short info 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tag").value("FINANCE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userAgeGroup").value("THREE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].progressdate").value(200L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].endSubscribeDate").value("2023-01-01"));

        // Mock 객체의 메서드가 정확한 파라미터로 호출되었는지 검증
//        Mockito.verify(profileService, Mockito.times(1)).findById(Mockito.anyString());
//        Mockito.verify(subscribeService, Mockito.times(1)).findSubscriptionsAndProductsByUserSortedByMoney(Mockito.anyString());
    }

    @DisplayName("포트폴리오 정보 표출 (일 기준 정렬)")
    @Test
    @WithMockUser(username = "testuser")
    void getUserSubscribeListOrderByDay() throws Exception {
        // 가상의 포트폴리오 정보 생성
        List<PortfolioResponse> portfolioList = new ArrayList<>();
        portfolioList.add(PortfolioResponse.builder()
                .subscribeId(1L)
                .presentMoney(1000L)
                .startMoney(500L)
                .subscribeDate(LocalDate.of(2022, 1, 1))
                .productId(1L)
                .userId(1L)
                .interestRate(0.02)
                .isDeposit(true)
                .longInfo("Long info 1")
                .maxProductMoney(10000L)
                .period(12)
                .productName("Product 1")
                .requiredStartMoney(500)
                .servicePurpose(ServicePurpose.FORCAR)
                .shortInfo("Short info 1")
                .tag(Tag.FINANCE)
                .userAgeGroup(UserAgeGroup.THREE)
                .progressdate(200L)
                .endSubscribeDate(LocalDate.of(2023, 1, 1))
                .build());


        // 모의 서비스 호출 시 리턴할 포트폴리오 정보 설정
        when(subscribeService.findSubscriptionsAndProductsByUserSortedByDays("testuser")).thenReturn(portfolioList);

        // /portfolio/days 엔드포인트 호출
        mockMvc.perform(MockMvcRequestBuilders.get("/subscribe/portfolio/days"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].subscribeId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].presentMoney").value(1000L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].startMoney").value(500L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].interestRate").value(0.02))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].deposit").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].longInfo").value("Long info 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].maxProductMoney").value(10000L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].period").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productName").value("Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requiredStartMoney").value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].servicePurpose").value("FORCAR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].shortInfo").value("Short info 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tag").value("FINANCE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userAgeGroup").value("THREE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].progressdate").value(200L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].endSubscribeDate").value("2023-01-01"));
    }

    @DisplayName("포트폴리오 찜 목록 표출")
    @Test
    @WithMockUser(username = "testuser")
    void getUserDibsList() throws Exception {
        // 가상의 Dibs 정보 생성
        List<Dibs> dibsList = new ArrayList<>();
        dibsList.add(new Dibs(user, product, LocalDate.now()));

        // Dibs 정보를 PortfolioResponse로 변환
        List<PortfolioResponse> convertedDibsList = dibsList.stream()
                .map(PortfolioResponse::dibsProfileFrom)
                .collect(Collectors.toList());

        // 모의 서비스 호출 시 리턴할 Dibs 정보 설정
        when(subscribeService.findDibsAndProductsByUser("testuser")).thenReturn(convertedDibsList);

        // /subscribe/portfolio/dibs 엔드포인트 호출
        mockMvc.perform(MockMvcRequestBuilders.get("/subscribe/portfolio/dibs"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tag").value("FINANCE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].longInfo").value("longInfo 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].period").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].interestRate").value(1.3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].deposit").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requiredStartMoney").value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].shortInfo").value("shortInfo 1"));

    }
}