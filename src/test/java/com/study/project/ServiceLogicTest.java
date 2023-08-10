package com.study.project;

import com.study.project.question.dto.QuestionRequestDto;
import com.study.project.question.dto.QuestionResponseDto;
import com.study.project.question.dto.QuestionSimpleResponseDto;
import com.study.project.question.entity.Question;
import com.study.project.question.service.QuestionService;
import com.study.project.user.dto.UserRequestDto;
import com.study.project.user.dto.UserResponseDto;
import com.study.project.user.entity.User;
import com.study.project.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
public class ServiceLogicTest {

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
                .password("pw1")
                .build();
        // When
        UserResponseDto user = userService.createUser(requestDto);
        // Then
        Assertions.assertThat(user.getEmail()).isEqualTo(requestDto.getEmail());
        Assertions.assertThat(user.getUsername()).isEqualTo(requestDto.getUsername());
    }

    @Test
    void createQuestionService() {
        // Given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .authorUsername("test")
                .subject("TestSubject")
                .content("TestContent")
                .build();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("test@test.com")
                .username("testUser")
                .password("pw1")
                .build();
        // When
        UserResponseDto user = userService.createUser(userRequestDto);
        QuestionResponseDto question = questionService.createQuestion(questionRequestDto);
        // Then
        Assertions.assertThat(user.getEmail()).isEqualTo(userRequestDto.getEmail());
        Assertions.assertThat(user.getUsername()).isEqualTo(userRequestDto.getUsername());
        Assertions.assertThat(question.getSubject()).isEqualTo(questionRequestDto.getSubject());
        Assertions.assertThat(question.getContent()).isEqualTo(questionRequestDto.getContent());
    }

    @Test
    @DisplayName("")
    void findQuestionList() {
        //Given
        QuestionRequestDto questionRequestDto1 = QuestionRequestDto.builder()
                .authorUsername("test")
                .subject("abc")
                .content("abc")
                .build();
        QuestionRequestDto questionRequestDto2 = QuestionRequestDto.builder()
                .authorUsername("test")
                .subject("def")
                .content("ghk")
                .build();
        QuestionRequestDto questionRequestDto3 = QuestionRequestDto.builder()
                .authorUsername("test")
                .subject("abc")
                .content("def")
                .build();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("test@test.com")
                .username("testUser")
                .password("pw1")
                .build();
        //When
        UserResponseDto user = userService.createUser(userRequestDto);
        QuestionResponseDto question1 = questionService.createQuestion(questionRequestDto1);
        QuestionResponseDto question2 = questionService.createQuestion(questionRequestDto2);
        QuestionResponseDto question3 = questionService.createQuestion(questionRequestDto3);
        //Then
        Page<QuestionSimpleResponseDto> de = questionService.findQuestionList(0, "de");
        System.out.println(de.getTotalElements());
        de.getContent().stream().forEach(q -> System.out.println(q.getSubject()));
    }

}
