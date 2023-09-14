package com.haedal.backend.auth.dto.request;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserRegisterRequest {
    private String id;
    private String password;
    private String name;
    private String phoneNumber;
    private UserAgeGroup userAgeGroup;
    private ServicePurpose servicePurpose;
    private String accountNumber;
    private Long asset;
    private int authNumber;



    public User toEntity(String password) {
        return User.builder()
                .id(this.id)
                .password(password)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .userAgeGroup(this.userAgeGroup)
                .servicePurpose(this.servicePurpose)
                .accountNumber(this.accountNumber)
                .asset(this.asset)
                .authNumber(this.authNumber)
                .build();
    }
}