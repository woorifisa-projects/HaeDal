package com.haedal.backend.auth.model;

import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.auth.dto.request.UserUpdateRequest;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import com.haedal.backend.subscribe.model.Subscribe;
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

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Subscribe> subscribes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Dibs> dibs;

    //활동 가능 고객인지 여부
    @Column(name = "user_status", columnDefinition = "BOOLEAN DEFAULT true")
    private boolean userStatus;

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateUserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }

    public void updateProfile(UserUpdateRequest userUpdateRequest)
    {
        this.name = userUpdateRequest.getName();
        this.phoneNumber = userUpdateRequest.getPhoneNumber();
        this.userAgeGroup = userUpdateRequest.getUserAgeGroup();
        this.servicePurpose = userUpdateRequest.getServicePurpose();
        this.accountNumber = userUpdateRequest.getAccountNumber();
        this.asset = userUpdateRequest.getAsset();
    }
    public void updateAsset(Long asset) {
        this.asset = asset;
    }

    public User(Long userId, String id) {
        this.userId = userId;
        this.id = id;
    }


}