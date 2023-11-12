package com.antongoranov.demojwt.security;

import com.antongoranov.demojwt.model.entity.TokenEntity;
import com.antongoranov.demojwt.model.entity.UserEntity;
import com.antongoranov.demojwt.repository.TokenRepository;
import com.antongoranov.demojwt.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Autowired
    public LogoutService(TokenRepository tokenRepository,
                         JwtProvider jwtProvider,
                         UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional // due to calling the @Transactional deleteUserToken
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader("Authentication");;
        String jwtToken;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        jwtToken = authHeader.substring(7);
        String username = jwtProvider.extractUsername(jwtToken);

        TokenEntity storedToken = tokenRepository.findByToken(jwtToken)
                .orElse(null);

        if(storedToken != null) {
            deleteUserToken(username);
        }

    }

    @Transactional
    public void deleteUserToken(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        tokenRepository.deleteByUser(user);
    }
}
