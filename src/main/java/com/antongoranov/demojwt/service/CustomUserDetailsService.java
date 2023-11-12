package com.antongoranov.demojwt.service;

import com.antongoranov.demojwt.model.entity.UserEntity;
import com.antongoranov.demojwt.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow();

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name())));
    }
}
