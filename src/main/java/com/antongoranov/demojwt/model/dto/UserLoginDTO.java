package com.antongoranov.demojwt.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    private String username;
    private String password;
}
