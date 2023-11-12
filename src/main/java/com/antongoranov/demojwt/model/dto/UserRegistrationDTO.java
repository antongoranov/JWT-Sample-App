package com.antongoranov.demojwt.model.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

}
