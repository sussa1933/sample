package com.study.project.answer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerRequestDto {

    private Long questionId;

    private String authorUsername;

    private String content;

}
