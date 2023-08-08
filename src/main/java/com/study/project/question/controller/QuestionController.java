package com.study.project.question.controller;

import com.study.project.question.dto.QuestionRequestDto;
import com.study.project.question.entity.Question;
import com.study.project.question.service.QuestionService;
import com.study.project.user.dto.UserRequestDto;
import com.study.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final UserService userService;

    private final QuestionService questionService;

    @GetMapping
    public void createQuestion() {
        userService.createUser(UserRequestDto.builder()
                .email("test@test.com")
                .username("testUser")
                .password("pw1")
                .build());
        QuestionRequestDto dto = QuestionRequestDto.builder()
                .authorId(1L)
                .content("test")
                .subject("test")
                .build();
        questionService.createQuestion(dto);
    }
}
