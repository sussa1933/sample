package com.study.project.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {

    private String userId;

    private String accessToken;


    public static LoginResponseDto of(String accessToken, Long userId) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .userId(String.valueOf(userId))
                .build();
    }


}
