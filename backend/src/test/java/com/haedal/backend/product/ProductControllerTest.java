package com.haedal.backend.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.log.service.LogService;
import com.haedal.backend.product.controller.ProductController;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.service.SubscribeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@WebMvcTest({ProductController.class})
public class ProductControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    protected UserService userService;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected ProfileService profileService;
    @MockBean
    protected SubscribeService subscribeService;
    @MockBean
    protected LogService logService;



    @MockBean
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        user = User.builder().userId(1L)
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



    @Test
    @DisplayName("전체 상품 조회 - productStatus=true인 상품들만 조회")
    @WithMockUser
    void getAllproductsInStatusTrue () throws Exception  {

        // GIVEN: 상품 목록을 설정하고 응답을 저장
       String productName = "첫거래 우대 정기예금";

//        product.setId(productId);
//        product.setProductStatus(true); // 상품의 상태를 true로 설정


        System.out.println(product);
        given(productService.findByProductId(1L)).willReturn(product);

        // WHEN & THEN: GET 요청을 수행하고 응답을 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .param("productId", productName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(productId)) // 응답에서 id 필드 확인
//                .andExpect(MockMvcResultMatchers.jsonPath("$.productStatus").value(true)) // 응답에서 productStatus 필드 확인
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("AllProductList check",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
    }


    @Test
    @DisplayName("태그별 상품 조회 - tag=FINANCE인 상품들만 조회")
    @WithMockUser
    void getFilterFINANACEProducts () throws Exception {
        // GIVEN: 상품 목록을 조회하고 응답을 저장
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/filter/FINANCE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseProducts = result.getResponse().getContentAsString();


        // WHEN: JSON 데이터를 파싱하여 tag 확인
        JsonNode jsonNode = objectMapper.readTree(responseProducts);


        // THEN: 각 상품 객체의 tag가 FINANCE인지 검증
        jsonNode.forEach(financeProductsInTag -> {
            JsonNode tagNode = financeProductsInTag.get("tag"); // "tag" 필드의 값을 가져옴
            assertNotNull(tagNode, "상품에 tag 필드가 없습니다."); // tag 필드가 없으면 예외 발생

            String tagValue = tagNode.asText(); // tag 값을 문자열로 변환

            // tag 값이 "FINANCE"인지 검증
            assertEquals("FINANCE", tagValue, "상품의 tag가 FINANCE가 아닙니다.");






        });

    }


    @Test
    @DisplayName("태그별 상품 조회 - tag=THEMA인 상품들만 조회")
    void getFilterTHEMAProducts () throws Exception {
        // GIVEN: 상품 목록을 조회하고 응답을 저장
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/filter/THEMA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseProducts = result.getResponse().getContentAsString();


        // WHEN: JSON 데이터를 파싱하여  확인
        JsonNode jsonNode = objectMapper.readTree(responseProducts);


        // THEN: 각 상품 객체의 tag가 THEMA인지 검증
        jsonNode.forEach(themaProductsInTag -> {
            JsonNode tagNode = themaProductsInTag.get("tag"); // "tag" 필드의 값을 가져옴
            assertNotNull(tagNode, "상품에 tag 필드가 없습니다."); // tag 필드가 없으면 예외 발생

            String tagValue = tagNode.asText(); // tag 값을 문자열로 변환

            // tag 값이 "THEMA"인지 검증
            assertEquals("THEMA", tagValue, "상품의 tag가 THEMA가 아닙니다.");
        });
    }


    @Test
    @DisplayName("상품 검색 - 로그인하지 않은 유저")
    @WithMockUser(username = "testuser")
    void getSearchProductNotLoggedInUser() throws Exception {

        // Mock 검색어 설정
        String searchKeyword = "예금";

        // Mock
        List<Product> products = new ArrayList<>();
        products.add(product);

        given(productService.findByProductNameLike("%" + searchKeyword + "%")).willReturn(products);


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/products", searchKeyword)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchKeyword)))
                .andDo(print())
                .andExpect(status().isOk());




        // when & then
//        mockMvc.perform(MockMvcRequestBuilders.get("/products", searchKeyword)
//                        .with(csrf())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(searchKeyword)))
//                .andDo(print())
//                .andExpect(status().isOk())
////                .andExpect(jsonPath("$[0].productStatus").value(true)) // 첫 번째 상품은 true 여야 함
//                .andDo(document("/{search}",
//                        responseFields(
//                                fieldWithPath("id").description("해당 검색 내역입니다.")
//                        )))
//                .andReturn();
    }



    @Test
    @DisplayName("상품 검색 - 로그인 한 유저")
    @WithMockUser(username = "testuser")
    void getSearchProductInLoggedInUser() throws Exception {

        String username = "testuser";
        Authentication authentication = new TestingAuthenticationToken(username, null, "USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock 검색어 설정
        String searchKeyword = "예금";

        // Mock
        List<Product> products = new ArrayList<>();
        products.add(product);

        given(productService.findByProductNameLike("%" + searchKeyword + "%")).willReturn(products);

//        given(logService.save(any(Log.class))).willReturn(Log);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/products", searchKeyword).with(authentication(authentication))
                        .with(csrf())
                        .param("searchKeyword", searchKeyword) // 검색어를 쿼리 파라미터로 전달
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchKeyword)))
                .andDo(print())
                .andExpect(status().isOk());




    }






}





