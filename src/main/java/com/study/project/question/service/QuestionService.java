package com.study.project.question.service;

import com.study.project.question.entity.Question;
import com.study.project.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question createQuestion(String title) {
        Question question = new Question();
        question.setTitle(title);
        Question saveQuestion = questionRepository.saveQuestion(question);
        return saveQuestion;
    }


}
