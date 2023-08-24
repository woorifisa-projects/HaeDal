package com.haedal.backend.auth.service;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.repository.UserRepository;
import com.haedal.backend.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${jwt.token.secret}")
    private String key;
    private final long expireTimeMs = 1000 * 60 * 60 * 24L; // 토큰 1일

    public UserDto register(UserRegisterRequest request) {
        String id = request.getId();
        userRepository.findById(request.getId())
                .ifPresent(user -> {
                    throw new RuntimeException(id + "는 이미 있습니다.");
                });

        User saveUser = userRepository.save(request.toEntity(bCryptPasswordEncoder.encode(request.getPassword())));
        return UserDto.fromEntity(saveUser);
    }

    public String login(String id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 ID입니다."));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return JwtUtil.createToken(id, expireTimeMs, key);
    }

}