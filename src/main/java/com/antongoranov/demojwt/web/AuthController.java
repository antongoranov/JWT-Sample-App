package com.antongoranov.demojwt.web;

import com.antongoranov.demojwt.model.dto.AuthenticationResponse;
import com.antongoranov.demojwt.model.dto.UserLoginDTO;
import com.antongoranov.demojwt.model.dto.UserRegistrationDTO;
import com.antongoranov.demojwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse>login(@RequestBody UserLoginDTO userLoginDTO) {

        AuthenticationResponse authenticationResponse = authService.login(userLoginDTO);

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRegistrationDTO userRegistrationDTO) {

        AuthenticationResponse response = authService.register(userRegistrationDTO);

        return ResponseEntity.ok(response);
    }
}
