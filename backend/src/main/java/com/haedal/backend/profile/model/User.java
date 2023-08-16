package com.haedal.backend.profile.model;

import com.haedal.backend.product.model.Subscribe;
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

    @Column(nullable = false,length = 20)
    private String name; // 이름

    @Column(name = "phone_number")
    private String phoneNumber; // 휴대폰 번호 입력

    @Enumerated(EnumType.STRING)
    @Column(name = "user_age_group")
    private UserAgeGroup userAgeGroup; // 연령대

    @Enumerated(EnumType.STRING)
    @Column(name = "service_purpose")
    private ServicePurpose servicePurpose; // 서비스 이용목적



    // Account 테이블로 분리했던 컬럼들 다시 User로 주입
    // 한 명의 유저는 한 개의 계좌를 가진다.
    @Column(name = "account_number")
    private String accountNumber; // 계좌 번호 입력

    @Column(name = "asset")
    private Long asset; // 자산


    @Column(name = "auth_number")
    private int authNumber; // 인증번호

    @OneToMany(mappedBy = "user")
    private List<Subscribe> subscribes; // 한 명의 사용자는 여러 개의 상품을 구입할 수 있다(상품 중복가입 X).

}