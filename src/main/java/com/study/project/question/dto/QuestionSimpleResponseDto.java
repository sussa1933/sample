package com.study.project.question.dto;

import com.study.project.answer.dto.AnswerResponseDto;
import com.study.project.answer.entity.Answer;
import com.study.project.question.entity.Question;
import com.study.project.user.dto.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionSimpleResponseDto {

    private Long id;

    private String subject;

    private UserResponseDto author;

    private List<AnswerResponseDto> answerList;

    private LocalDateTime createDate;

    public static QuestionSimpleResponseDto of(Question question) {
        Set<Answer> answerSet = question.getAnswerList();
        List<Answer> answerList = new ArrayList<>(answerSet);
        List<AnswerResponseDto> answerResponseList = answerList.stream().map(AnswerResponseDto::of)
                .toList();
        return QuestionSimpleResponseDto.builder()
                .id(question.getId())
                .subject(question.getSubject())
                .author(UserResponseDto.of(question.getAuthor()))
                .answerList(answerResponseList)
                .createDate(question.getCreateDate())
                .build();
    }

}
