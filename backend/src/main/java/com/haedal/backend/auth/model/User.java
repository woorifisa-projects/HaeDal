package com.haedal.backend.auth.model;

import com.haedal.backend.product.model.Subscribe;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true,nullable = false)
    private String id;

    private String password;

    @Column(nullable = false,length = 20)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_age_group")
    private UserAgeGroup userAgeGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_purpose")
    private ServicePurpose servicePurpose;

    @Column(name = "account_number")
    private String accountNumber; // 계좌 번호 입력

    @Column(name = "asset")
    private Long asset; // 자산

    @Column(name = "auth_number")
    private int authNumber; // 인증번호

    @OneToMany(mappedBy = "user")
    private List<Subscribe> subscribes;

}