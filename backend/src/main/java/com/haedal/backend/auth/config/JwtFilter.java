package com.haedal.backend.auth.config;

import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {


    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization : {}",authorization);

        if(authorization == null || !authorization.startsWith("Bearer" )){
            log.info("authorization");
            filterChain.doFilter(request,response);
            return;
        }

        // Token 꺼내기
        String token = authorization.substring(7);

        // Token Expired되었는지 여부
        if(JwtUtil.isExpired(token,secretKey)){
            log.error("Token 이 만료 되었습니다.");
            filterChain.doFilter(request,response);
            return;
        }

        // UserName Token에서 꺼내기
        String userId = JwtUtil.getId(token,secretKey);
        log.info("여기에요 여기");
        log.info(userId);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId,null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        // Detail 넣어주기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info(authenticationToken.getName());
        filterChain.doFilter(request,response);
    }
}
