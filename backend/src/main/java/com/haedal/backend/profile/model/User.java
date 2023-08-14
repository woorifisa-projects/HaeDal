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
    private Long id;

    @Column(name = "user_id",nullable = false,length = 20)
    private String userId; // 회원가입시 필요한 ID

    @Column(nullable = false,length = 20)
    private String password; // 회원가입시 필요한 PW

    @Column
    private String name; // 이름 적으셈

    @Column(name = "phone_number")
    private Long phoneNumber; // 휴대폰 번호 입력 - 숫자 11자리이므로 int 값 초과하므로 Long으로 변경해주자(예정).

    @Column(name = "user_age_group")
    private UserAgeGroup userAgeGroup; // 연령대 적으셈

    @Column(name = "account_number")
    private Long accountNumber; // 계좌 번호 입력 - int 값 초과하므로 Long으로 변경해주자(예정).

    @Column(name = "auth_number")
    private Long authNumber;

    @Column
    private Long asset; // 계좌 번호 입력 - int 값 초과하므로 Long으로 변경해주자(예정).

    @Column(name = "service_purpose")
    private ServicePurpose servicePurpose;


}