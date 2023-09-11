package com.haedal.backend.profile.service;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserUpdateRequest;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    // Service -> Repository의존하므로 의존성 주입 - 생성자 방식으로
    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    @Override
    public User findById(Long userId) {
        return profileRepository.findById(userId).orElse(null);
    }

    @Override
    public User findById(String id) {
        return profileRepository.findById(id);
    }

    @Override
    public User save(User user) { return profileRepository.save(user); }

    public Long getUserId(User user){return user.getUserId();}
}