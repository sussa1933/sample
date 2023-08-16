package com.study.project.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

    @Size(min = 3, max = 20)
    @NotEmpty(message = "회원 ID는 필수 입력사항입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력사항 입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인란은 필수 입력 사항 입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수 입력사항 입니다.")
    private String email;

}
