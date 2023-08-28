package com.haedal.backend.auth.dto.response;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateResponse {
    private String id;
    private String password;
    private String name;
    private String phoneNumber;
    private ServicePurpose servicePurpose;
    private UserAgeGroup userAgeGroup;
    private String accountNumber;
    private Long asset;
    private int authNumber;

    @Builder
    public UserUpdateResponse(String name, String phoneNumber, ServicePurpose servicePurpose, UserAgeGroup userAgeGroup, String accountNumber, Long asset, int authNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.servicePurpose = servicePurpose;
        this.userAgeGroup = userAgeGroup;
        this.accountNumber = accountNumber;
        this.asset = asset;
        this.authNumber = authNumber;
    }

    public static UserUpdateResponse UserProfileUpdateInfoFrom(User user){
        return new UserUpdateResponse(user.getName(),user.getPhoneNumber(),user.getServicePurpose(),user.getUserAgeGroup(),user.getAccountNumber(),user.getAsset(),user.getAuthNumber());
    }
}
