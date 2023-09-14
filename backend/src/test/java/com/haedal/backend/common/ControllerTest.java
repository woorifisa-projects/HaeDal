package com.haedal.backend.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haedal.backend.Dibs.controller.DibsController;
import com.haedal.backend.Dibs.service.DibsService;
import com.haedal.backend.auth.controller.UserController;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.log.service.LogService;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.jpa.util.JpaMetamodel;

@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@WebMvcTest({
        UserController.class,
        DibsController.class
})
//@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
public abstract class ControllerTest {
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
    protected ProductService productService;

    @MockBean
    protected DibsService dibsService;


}
