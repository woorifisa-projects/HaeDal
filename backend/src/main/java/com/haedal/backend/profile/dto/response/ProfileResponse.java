package com.haedal.backend.profile.dto.response;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponse {


    private String id;
    private String password;
    private String name;
    private String phoneNumber;
    private ServicePurpose servicePurpose;
    private UserAgeGroup userAgeGroup;
    private String accountNumber;
    private Long asset;
    private int authNumber;

    //    private Long asset;
    @Builder
    public ProfileResponse(String name) {
        this.name = name;
    }

    public ProfileResponse(String password, String name) {
        this.password = password;
        this.name = name;
    }

    @Builder
    public ProfileResponse(String name, String phoneNumber, ServicePurpose servicePurpose, UserAgeGroup userAgeGroup) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.servicePurpose = servicePurpose;
        this.userAgeGroup = userAgeGroup;
    }

    @Builder
    public ProfileResponse(String name, String phoneNumber, ServicePurpose servicePurpose, UserAgeGroup userAgeGroup, String accountNumber, Long asset, int authNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.servicePurpose = servicePurpose;
        this.userAgeGroup = userAgeGroup;
        this.accountNumber = accountNumber;
        this.asset = asset;
        this.authNumber = authNumber;
    }

    @Builder
    public ProfileResponse(String id, String password, String name, String phoneNumber, ServicePurpose servicePurpose, UserAgeGroup userAgeGroup, String accountNumber, Long asset, int authNumber) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.servicePurpose = servicePurpose;
        this.userAgeGroup = userAgeGroup;
        this.accountNumber = accountNumber;
        this.asset = asset;
        this.authNumber = authNumber;
    }


    public static ProfileResponse userNameInfoFrom(User user){
        return new ProfileResponse(user.getName());
    }

    public static ProfileResponse profileInfoFrom(User user) {
        return new ProfileResponse(user.getName(), user.getPhoneNumber(), user.getServicePurpose(), user.getUserAgeGroup());
    }

    public static ProfileResponse allUserInfoFrom(User user){
        return new ProfileResponse(user.getId(), user.getPassword(),user.getName(),user.getPhoneNumber(),user.getServicePurpose(),user.getUserAgeGroup(),user.getAccountNumber(),user.getAsset(),user.getAuthNumber());
    }

    public static ProfileResponse userProfileUpdateFrom(User user){
        return new ProfileResponse(user.getName(),user.getPhoneNumber(),user.getServicePurpose(),user.getUserAgeGroup(),user.getAccountNumber(),user.getAsset(),user.getAuthNumber());
    }

    public static ProfileResponse userCheckPwFrom(User user){
        return new ProfileResponse(user.getPassword(),user.getName());
    }
}