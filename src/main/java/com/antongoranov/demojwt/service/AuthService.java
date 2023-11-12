package com.antongoranov.demojwt.service;

import com.antongoranov.demojwt.model.dto.AuthenticationResponse;
import com.antongoranov.demojwt.model.dto.UserLoginDTO;
import com.antongoranov.demojwt.model.dto.UserRegistrationDTO;
import com.antongoranov.demojwt.model.entity.TokenEntity;
import com.antongoranov.demojwt.model.entity.UserEntity;
import com.antongoranov.demojwt.model.enums.RoleEnum;
import com.antongoranov.demojwt.repository.TokenRepository;
import com.antongoranov.demojwt.repository.UserRepository;
import com.antongoranov.demojwt.security.JwtProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider,
                       TokenRepository tokenRepository,
                       UserDetailsService userDetailsService,
                       AuthenticationManager authenticationManager) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.tokenRepository = tokenRepository;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse register(UserRegistrationDTO userRegistrationDTO) {

        UserEntity newUser = UserEntity.builder()
                .username(userRegistrationDTO.getUsername())
                .email(userRegistrationDTO.getEmail())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .role(RoleEnum.USER)
                .build();

        userRepository.save(newUser);

        UserDetails savedUser = userDetailsService.loadUserByUsername(newUser.getUsername());

        //get the jwt token
        String jwtTokenForUser = jwtProvider.generateToken(savedUser);

        saveUserToken(newUser, jwtTokenForUser);

        //return the AuthenticationResponse
        return AuthenticationResponse.builder()
                .token(jwtTokenForUser)
                .build();
    }

    @Transactional // due to calling the @Transactional refreshTokenForUser
    public AuthenticationResponse login(UserLoginDTO userLoginDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getUsername(),
                        userLoginDTO.getPassword()));

        UserEntity user = userRepository.findByUsername(
                userLoginDTO.getUsername())
                .orElseThrow();

        UserDetails fetchedUser = userDetailsService.loadUserByUsername(user.getUsername());

        String jwtTokenForUser = jwtProvider.generateToken(fetchedUser);

        TokenEntity token = tokenRepository.findByUser(user).orElse(null);

        if(token != null){
            refreshTokenForUser(user, jwtTokenForUser);
        } else {
            saveUserToken(user, jwtTokenForUser);
        }

        return AuthenticationResponse.builder()
                .token(jwtTokenForUser)
                .build();
    }

    @Transactional
    private void refreshTokenForUser(UserEntity user, String jwtToken) {
        tokenRepository.refreshTokenByUser(user, jwtToken);
    }

    private void saveUserToken(UserEntity user, String jwtToken){

        TokenEntity tokenForUser = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .build();

        tokenRepository.save(tokenForUser);
    }
}
