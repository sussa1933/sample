package com.study.project.answer.dto;

import com.study.project.answer.entity.Answer;
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
public class AnswerResponseDto {

    private Long id;

    private Long questionId;

    private String content;

    private UserResponseDto author;

    List<UserResponseDto> voter;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    public static AnswerResponseDto of(Answer answer) {
        Set<User> voterSet = answer.getVoter();
        List<User> voterList = new ArrayList<>(voterSet);
        List<UserResponseDto> responseDtoList = voterList.stream().map(UserResponseDto::of)
                .toList();
        return AnswerResponseDto.builder()
                .id(answer.getId())
                .questionId(answer.getQuestion().getId())
                .content(answer.getContent())
                .voter(responseDtoList)
                .author(UserResponseDto.of(answer.getAuthor()))
                .createDate(answer.getCreateDate())
                .modifyDate(answer.getModifyDate())
                .build();
    }

}
