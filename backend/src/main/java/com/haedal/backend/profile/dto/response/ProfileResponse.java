package com.haedal.backend.profile.dto.response;

import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.User;
import com.haedal.backend.profile.model.UserAgeGroup;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponse {

    private String name;
    private String id;
    private ServicePurpose servicePurpose;
    private UserAgeGroup userAgeGroup;
    private Long asset;

    @Builder
    public ProfileResponse(String name, String id, ServicePurpose servicePurpose, UserAgeGroup userAgeGroup, Long asset) {
        this.name = name;
        this.id = id;
        this.servicePurpose = servicePurpose;
        this.userAgeGroup = userAgeGroup;
        this.asset = asset;
    }


    public static ProfileResponse profileInfoFrom(User user) {
        return new ProfileResponse(user.getName(), user.getId(), user.getServicePurpose(), user.getUserAgeGroup(), user.getAsset());
    }
}