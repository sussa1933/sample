package com.study.project;

import com.study.project.question.dto.QuestionRequestDto;
import com.study.project.question.entity.Question;
import com.study.project.question.service.QuestionService;
import com.study.project.user.dto.UserRequestDto;
import com.study.project.user.entity.User;
import com.study.project.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SampleTest {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Test
    void createUserTest() {
        // Given
        UserRequestDto requestDto = UserRequestDto.builder()
                .email("test@test.com")
                .username("testUser")
                .password1("pw1")
                .password2("pw1")
                .build();
        // When
        User user = userService.createUser(requestDto);
        // Then
        Assertions.assertThat(user.getEmail()).isEqualTo(requestDto.getEmail());
        Assertions.assertThat(user.getUsername()).isEqualTo(requestDto.getUsername());
    }

    @Test
    void createQuestionService() {
        // Given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .authorId(1L)
                .subject("TestSubject")
                .content("TestContent")
                .build();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("test@test.com")
                .username("testUser")
                .password1("pw1")
                .password2("pw1")
                .build();
        // When
        User user = userService.createUser(userRequestDto);
        Question question = questionService.createQuestion(questionRequestDto);
        // Then
        Assertions.assertThat(user.getEmail()).isEqualTo(userRequestDto.getEmail());
        Assertions.assertThat(user.getUsername()).isEqualTo(userRequestDto.getUsername());
        Assertions.assertThat(question.getSubject()).isEqualTo(questionRequestDto.getSubject());
        Assertions.assertThat(question.getContent()).isEqualTo(questionRequestDto.getContent());
    }
}
