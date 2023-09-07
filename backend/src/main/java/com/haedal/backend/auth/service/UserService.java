package com.haedal.backend.auth.service;

import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserRegisterRequest;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.repository.UserRepository;
import com.haedal.backend.auth.util.JwtUtil;
import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.model.LogType;
import com.haedal.backend.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LogService logService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${jwt.token.secret}")
    private String key;
    private final long expireTimeMs = 1000 * 60 * 60; // 토큰 1일

    @Transactional
    public UserDto register(UserRegisterRequest request) {
        String id = request.getId();
        userRepository.findById(request.getId())
                .ifPresent(user -> {
                    throw new RuntimeException(id + "는 이미 있습니다.");
                });

        User saveUser = userRepository.save(request.toEntity(bCryptPasswordEncoder.encode(request.getPassword())));
        // 로그를 위한 비즈니스 로직
        LogType logType = LogType.valueOf("SIGNUP");
        LocalDateTime logDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9);
        String logRegister = saveUser.getName()+" 고객님 회원가입";
        Log savelog = logService.save( new Log(saveUser, logType, logDateTime,logRegister));

        return UserDto.fromEntity(saveUser);
    }

    @Transactional
    public String login(String id, String password) {

        User user = userRepository.findById(id) // ID 체크
                .orElseThrow(() -> new RuntimeException("가입되지 않은 ID입니다."));
        // PW 체크
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        // 휴면유저체크
        if(!user.isUserStatus())
        {
            throw new RuntimeException("휴면처리된 아이디입니다");
        }

        LogType logType = LogType.valueOf("LOGIN");
        LocalDateTime logDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9);
        String logLogIn = user.getName()+" 고객님 로그인";
        Log savelog = logService.save( new Log(user, logType, logDateTime,logLogIn));

        return JwtUtil.createToken(id, expireTimeMs, key);
    }



    @Transactional
    public void deleteById(String id){
        userRepository.deleteById(id);
    }

}