package com.study.project.question.controller;

import com.study.project.question.entity.Question;
import com.study.project.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/init")
    public String init(
            @RequestParam String title
    ) {
        Question question = questionService.createQuestion(title);
        return question.getTitle() + question.getId();
    }

}
