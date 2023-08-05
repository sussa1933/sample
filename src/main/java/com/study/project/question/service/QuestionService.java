package com.study.project.question.service;

import com.study.project.question.dto.QuestionRequestDto;
import com.study.project.question.entity.Question;
import com.study.project.question.repository.QuestionJpaRepository;
import com.study.project.user.entity.User;
import com.study.project.user.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionJpaRepository questionJpaRepository;

    private final UserJpaRepository userJpaRepository;

    @Transactional
    public Question createQuestion(QuestionRequestDto dto) {
        String subject = dto.getSubject();
        String content = dto.getContent();
        Long authorId = dto.getAuthorId();
        Optional<User> findUser = userJpaRepository.findById(authorId);
        User user = findUser.orElseThrow(() -> new RuntimeException("NOT FOUND, Bad Request"));
        Question question = Question.createQuestion(subject, content);
        question.addAuthor(user);
        return questionJpaRepository.save(question);
    }




}
