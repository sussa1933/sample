package com.study.project.question.repository.impl;

import com.study.project.question.entity.Question;
import com.study.project.question.repository.QuestionJpaRepository;
import com.study.project.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class QuestionRepositoryImpl implements QuestionRepository {


    private final QuestionJpaRepository jpaRepository;


    @Override
    public Question saveQuestion(Question question) {
        Question save = jpaRepository.save(question);
        return save;
    }
}
