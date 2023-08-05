package com.study.project.question.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionRequestDto {

    private Long authorId;

    private String subject;

    private String content;


}
