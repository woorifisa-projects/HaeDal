package com.haedal.backend.auth.dto;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserDto {
    private Long userId;
    private String id;
    private String password;
    private String name;
    private String phoneNumber;
    private UserAgeGroup userAgeGroup;
    private ServicePurpose servicePurpose;
    private String accountNumber;
    private Long asset;
    private int authNumber;

    public static UserDto fromEntity(User user)  {
        UserDto userDto = UserDto.builder()
                .userId(user.getUserId())
                .id(user.getId())
                .password(user.getPassword())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .userAgeGroup(user.getUserAgeGroup())
                .servicePurpose(user.getServicePurpose())
                .accountNumber(user.getAccountNumber())
                .asset(user.getAsset())
                .authNumber(user.getAuthNumber())
                .build();
        return userDto;
    }
}
