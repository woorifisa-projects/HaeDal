package com.haedal.backend.profile.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // PK

    @Column(nullable = false,length = 20)
    private String Id; // 회원가입시 필요한 ID

    @Column(nullable = false,length = 20)
    private String password; // 회원가입시 필요한 PW

    @Column
    private String name; // 이름

    @Column(name = "phone_number")
    private Long phoneNumber; // 휴대폰 번호 입력

    @Column(name = "user_age_group")
    private UserAgeGroup userAgeGroup; // 연령대

    @Column(name = "account_number")
    private Long accountNumber; // 계좌 번호 입력

    @Column(name = "auth_number")
    private Long authNumber;

    @Column
    private Long asset; // 계좌 번호 입력

    @Column(name = "service_purpose")
    private ServicePurpose servicePurpose;


}