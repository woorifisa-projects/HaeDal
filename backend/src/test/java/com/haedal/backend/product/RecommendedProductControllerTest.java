//package com.haedal.backend.product;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.haedal.backend.Dibs.model.Dibs;
//import com.haedal.backend.auth.model.User;
//import com.haedal.backend.auth.service.UserService;
//import com.haedal.backend.common.ControllerTest;
//import com.haedal.backend.log.model.LogType;
//import com.haedal.backend.log.service.LogService;
//import com.haedal.backend.product.controller.ProductController;
//import com.haedal.backend.product.controller.RecommendedProductController;
//import com.haedal.backend.product.dto.response.ProductResponse;
//import com.haedal.backend.product.model.Product;
//import com.haedal.backend.product.model.Tag;
//import com.haedal.backend.product.repository.ProductRepository;
//import com.haedal.backend.product.service.RecommendedProductService;
//import com.haedal.backend.profile.model.ServicePurpose;
//import com.haedal.backend.profile.model.UserAgeGroup;
//import com.haedal.backend.profile.service.ProfileService;
//import com.haedal.backend.subscribe.service.SubscribeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
//import org.springframework.restdocs.operation.preprocess.Preprocessors;
//import org.springframework.restdocs.payload.PayloadDocumentation;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//
//import java.nio.charset.Charset;
//import java.time.LocalDate;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static net.bytebuddy.matcher.ElementMatchers.any;
//import static org.hamcrest.Matchers.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//public class RecommendedProductControllerTest extends ControllerTest {
//
//
//    @InjectMocks
//    private RecommendedProductController recommendedProductController;
//    @MockBean
//    private RecommendedProductService recommendedProductService;
//    @MockBean
//    private ProductRepository productRepository;
//
//
//    private ObjectMapper objectMapper;
//    private User user;
//    private Product product;
//    private Product product1;
//    private Product product2;
//    private Product product3;
//    private static final MediaType TEXT_PLAIN_UTF8 = new MediaType("text", "plain", Charset.forName("UTF-8"));
//
//    @BeforeEach
//    void setUp() {
//        objectMapper = new ObjectMapper();
//        user =User.builder().userId(1L)
//                .id("testuser")
//                .authNumber(12345)
//                .asset(1000000L)
//                .build();
//
//        product = Product.builder()
//                .productId(1L)
//                .requiredStartMoney(500)
//                .maxProductMoney(10000L)
//                .servicePurpose(ServicePurpose.MOKDON)
//                .userAgeGroup(UserAgeGroup.THREE)
//                .tag(Tag.FINANCE)
//                .productName("첫거래 우대 정기예금")
//                .shortInfo("shortInfo 1")
//                .longInfo("longInfo 1")
//                .period(12)
//                .interestRate(1.3)
//                .isDeposit(true)
//                .productStatus(true)
//                .build();
//
//        product1 = Product.builder()
//                .productId(2L)
//                .requiredStartMoney(500)
//                .maxProductMoney(10000L)
//                .servicePurpose(ServicePurpose.MOKDON)
//                .userAgeGroup(UserAgeGroup.THREE)
//                .tag(Tag.FINANCE)
//                .productName("첫거래 우대 정기예금")
//                .shortInfo("shortInfo 1")
//                .longInfo("longInfo 1")
//                .period(12)
//                .interestRate(1.3)
//                .isDeposit(true)
//                .productStatus(true)
//                .build();
//
//
//        product2 = Product.builder()
//                .productId(3L)
//                .requiredStartMoney(500)
//                .maxProductMoney(10000L)
//                .servicePurpose(ServicePurpose.MOKDON)
//                .userAgeGroup(UserAgeGroup.THREE)
//                .tag(Tag.FINANCE)
//                .productName("첫거래 우대 정기예금")
//                .shortInfo("shortInfo 1")
//                .longInfo("longInfo 1")
//                .period(12)
//                .interestRate(1.3)
//                .isDeposit(true)
//                .productStatus(true)
//                .build();
//
//
//        product3 = Product.builder()
//                .productId(4L)
//                .requiredStartMoney(500)
//                .maxProductMoney(10000L)
//                .servicePurpose(ServicePurpose.MOKDON)
//                .userAgeGroup(UserAgeGroup.THREE)
//                .tag(Tag.FINANCE)
//                .productName("첫거래 우대 정기예금")
//                .shortInfo("shortInfo 1")
//                .longInfo("longInfo 1")
//                .period(12)
//                .interestRate(1.3)
//                .isDeposit(true)
//                .productStatus(true)
//                .build();
//
//        // 모의 객체에 대한 행동 설정
//        Mockito.when(profileService.findById(anyString())).thenReturn(user);
//        Mockito.when(productService.findByProductId(Mockito.anyLong())).thenReturn(product);
//    }
//
//
//
//    @DisplayName("추천상품 페이지 - 로그인한 회원에게 자산별 추천 상품이 반환되는지")
//    @Test
//    @WithMockUser(username = "testuser")
////    @WithUserDetails(value = "yeji", userDetailsServiceBeanName = "userDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    void getRecommendedByAssetForLoggedInUser() throws Exception {
//
//        // 1. Authentication에서 user의 userId를 뽑아온다
////        Long userId = user.getUserId();
//
//        // GIVEN: Mock 데이터 설정
//        String username = "testuser";
//        Authentication authentication = new TestingAuthenticationToken(user.getName(), null, "USER");
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
//        List<Product>products = new ArrayList<>();
//                products.add(product1);
//                products.add(product2);
//                products.add(product3);
//
//
//        given(recommendedProductService.filterProductsByAsset(user.getUserId()))
//                .willReturn(products);
//
//
////
////        // 고객 자산에 따른 추천 상품이 있을 때의 처리
////        // recommendedProductService의 filterProductsByAsset 메서드를 Mock으로 설정하여 상품 ID 목록을 반환하도록 함
////        when(recommendedProductService.filterProductsByAsset(user.getUserId())).thenReturn();
//
//
//        // THEN: 응답의 상태 코드와 내용을 검증
//        mockMvc.perform(MockMvcRequestBuilders.get("/recommendedProduct")
//                        .with(authentication(authentication))
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(lessThanOrEqualTo(3)))
//                .andDo(MockMvcRestDocumentation.document("RecommendedProducts for loggedIn-user",
//                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
//                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
//
//
//
//}
//
//
//
//    @DisplayName("추천상품 페이지 - 로그인한 회원에게 이용목적별 추천 상품이 반환되는지")
//    @Test
//    @WithMockUser(username = "testuser")
//    void getRecommnededByServicePurposeTest() throws Exception {
//
//        // GIVEN: Mock 데이터 설정
//        String username = "testuser";
//        Authentication authentication = new TestingAuthenticationToken(user.getName(), null, "USER");
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
//        List<Product> products = new ArrayList<>();
//        products.add(product1);
//        products.add(product2);
//        products.add(product3);
//
//
//        given(recommendedProductService.filterProductsByServicePurpose(user.getUserId()))
//                .willReturn(products);
//
//
//        // THEN: 응답의 상태 코드와 내용을 검증
//        mockMvc.perform(MockMvcRequestBuilders.get("/recommendedProduct")
//                        .with(authentication(authentication))
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(lessThanOrEqualTo(3)))
//                .andDo(MockMvcRestDocumentation.document("RecommendedProducts for loggedIn-user",
//                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
//                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
//
//    }
//
//
//
//
//    @DisplayName("추천상품 페이지 - 로그인한 회원에게 연령대별 추천 상품이 반환되는지")
//    @Test
//    @WithMockUser(username = "testuser")
//    void getRecommnededByUserAgeGroupTest() throws Exception {
//
//        // GIVEN: Mock 데이터 설정
//        String username = "testuser";
//        Authentication authentication = new TestingAuthenticationToken(user.getName(), null, "USER");
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
//        List<Product> products = new ArrayList<>();
//        products.add(product1);
//        products.add(product2);
//        products.add(product3);
//
//
//        given(recommendedProductService.filterProductsByUserAgeGroup(user.getUserId()))
//                .willReturn(products);
//
//
//        // THEN: 응답의 상태 코드와 내용을 검증
//        mockMvc.perform(MockMvcRequestBuilders.get("/recommendedProduct")
//                        .with(authentication(authentication))
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(lessThanOrEqualTo(3)))
//                .andDo(MockMvcRestDocumentation.document("RecommendedProducts for loggedIn-user",
//                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
//                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
//
//
//        }
//
//
//
//
//    @Test
//    @DisplayName("사용자가 로그인하지 않은 경우 - 추천상품을 조회할 수 없음")
//    void notLoggedInUser() throws Exception {
//        // GIVEN
//        SecurityContextHolder.getContext().setAuthentication(null);
//
//
//        // WHEN: '/error' 엔드포인트 호출
//        mockMvc.perform(MockMvcRequestBuilders.get("/error"))
//                .andExpect(status().isUnauthorized())
//                .andDo(MockMvcRestDocumentation.document("userLoggedOut",
//                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
//                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
//
//
//
//    }
//
//
//}
