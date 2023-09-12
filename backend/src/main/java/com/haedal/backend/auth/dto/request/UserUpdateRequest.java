package com.haedal.backend.auth.dto.request;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String name;
    private String phoneNumber;
    private UserAgeGroup userAgeGroup;
    private ServicePurpose servicePurpose;
    private String accountNumber;
    private Long asset;
    private int authNumber;
//    private String adminKey;
}
