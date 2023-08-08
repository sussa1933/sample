package com.study.project.question.dto;


import com.study.project.answer.dto.AnswerResponseDto;
import com.study.project.answer.entity.Answer;
import com.study.project.question.entity.Question;
import com.study.project.user.dto.UserResponseDto;
import com.study.project.user.entity.User;
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
public class QuestionResponseDto {

    private Long id;

    private String subject;

    private String content;

    private UserResponseDto author;

    private List<AnswerResponseDto> answerList;

    private List<UserResponseDto> voter;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    public static QuestionResponseDto of(Question question) {
        Set<User> voterSet = question.getVoter();
        List<User> voterList = new ArrayList<>(voterSet);
        List<UserResponseDto> voterResponseList = voterList.stream().map(UserResponseDto::of)
                .toList();
        Set<Answer> answerSet = question.getAnswerList();
        List<Answer> answerList = new ArrayList<>(answerSet);
        List<AnswerResponseDto> answerResponseList = answerList.stream().map(AnswerResponseDto::of)
                .toList();
        return QuestionResponseDto.builder()
                .id(question.getId())
                .subject(question.getSubject())
                .content(question.getContent())
                .author(UserResponseDto.of(question.getAuthor()))
                .answerList(answerResponseList)
                .voter(voterResponseList)
                .createDate(question.getCreateDate())
                .modifyDate(question.getModifyDate())
                .build();
    }

}
