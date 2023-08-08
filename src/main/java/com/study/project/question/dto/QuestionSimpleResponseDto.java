package com.study.project.question.dto;

import com.study.project.question.entity.Question;
import com.study.project.user.dto.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionSimpleResponseDto {

    private Long id;

    private String subject;

    private UserResponseDto author;

    private LocalDateTime createDate;

    public static QuestionSimpleResponseDto of(Question question) {
        return QuestionSimpleResponseDto.builder()
                .id(question.getId())
                .subject(question.getSubject())
                .author(UserResponseDto.of(question.getAuthor()))
                .createDate(question.getCreateDate())
                .build();
    }

}
