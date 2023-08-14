package com.haedal.backend.profile.service;

import com.haedal.backend.profile.model.User;
import com.haedal.backend.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    // Service -> Repository의존하므로 의존성 주입 - 생성자 방식으로
    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public User findById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }
}