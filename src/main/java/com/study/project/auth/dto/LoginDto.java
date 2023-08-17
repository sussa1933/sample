package com.study.project.auth.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginDto {

    private String username;

    private String password;

}
