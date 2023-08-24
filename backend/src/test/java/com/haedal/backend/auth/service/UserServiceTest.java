package com.haedal.backend.auth.service;

import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.repository.UserRepository;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    UserServiceTest() {
    }

    @BeforeEach
    void setUp(){
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @AfterEach
    void clean(){ userRepository.deleteAll(); }


    @Test
    @DisplayName("회원가입 성공")
    void test1(){
        // given
        UserRegisterRequest request = UserRegisterRequest.builder()
                .id("dragonmin25")
                .password(passwordEncoder.encode("1234"))
                .name("yongmin")
                .phoneNumber("010-1234-5678")
                .userAgeGroup(UserAgeGroup.valueOf("ONE"))
                .servicePurpose(ServicePurpose.valueOf("MOKDON"))
                .accountNumber("1")
                .asset(null)
                .authNumber(1)
                .build();

        // when
        userService.register(request);

        // then
        assertEquals(1, userRepository.count());

        User user = userRepository.findById("dragonmin25").get();
        assertEquals("dragonmin25", user.getId());
        assertTrue(passwordEncoder.matches("1234",user.getPassword()));
        assertEquals("yongmin", user.getName());
        assertEquals("010-1234-5678", user.getPhoneNumber());
        assertEquals(UserAgeGroup.ONE, user.getUserAgeGroup());
        assertEquals(ServicePurpose.MOKDON, user.getServicePurpose());
        assertEquals("1", user.getAccountNumber());
        assertNull(user.getAsset());


    }

}