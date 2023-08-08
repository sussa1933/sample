package com.study.project.user.dto;

import com.study.project.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private String email;

    private String username;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .createDate(user.getCreateDate())
                .modifyDate(user.getModifyDate())
                .build();
    }

}
