package com.study.project.user.controller;

import com.study.project.exception.ErrorCode;
import com.study.project.exception.ServiceLogicException;
import com.study.project.user.dto.UserCreateForm;
import com.study.project.user.dto.UserRequestDto;
import com.study.project.user.dto.UserResponseDto;
import com.study.project.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView(
                "login_form"
        );
    }

    @GetMapping("/signup")
    public ModelAndView createUser() {
        UserCreateForm emptyUserCreateForm = new UserCreateForm();
        return new ModelAndView(
                "signup_form",
                Map.of("userCreateForm",emptyUserCreateForm)
        );
    }

    @PostMapping("/signup")
    public ModelAndView signup(
            @Valid UserCreateForm userCreateForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView(
                    "signup_form"
            );
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2","passwordInCorrect","2개의 패스워드가 일치하지 않습니다.");
            return new ModelAndView(
                    "signup_form"
            );
        }
        try {
            UserRequestDto requestDto = UserRequestDto.of(userCreateForm);
            UserResponseDto createUser = userService.createUser(requestDto);
            return new ModelAndView(
                    "redirect:/"
            );
        } catch (ServiceLogicException se) {
            if (se.getErrorCode().equals(ErrorCode.USER_EXIST)) {
                se.printStackTrace();
                bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            }
            return new ModelAndView(
                    "signup_form"
            );
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return new ModelAndView(
                    "signup_form"
            );
        }
    }
}
