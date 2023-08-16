package com.haedal.backend.profile.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "User")
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


    @Column(name = "auth_number")
    private Long authNumber; //

    @Column(name = "service_purpose")
    private ServicePurpose servicePurpose; // 서비스 이용목적

    @OneToMany(mappedBy = "user")
    private List<Account> accounts; // 한 사용자는 여러개의 계좌를 가질 수 있다.

}