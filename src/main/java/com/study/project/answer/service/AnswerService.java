package com.study.project.answer.service;


import com.study.project.answer.dto.AnswerRequestDto;
import com.study.project.answer.dto.AnswerResponseDto;
import com.study.project.answer.entity.Answer;
import com.study.project.answer.repository.AnswerJpaRepository;
import com.study.project.question.entity.Question;
import com.study.project.question.repository.QuestionJpaRepository;
import com.study.project.question.service.QuestionService;
import com.study.project.user.entity.User;
import com.study.project.user.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionJpaRepository questionJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final AnswerJpaRepository answerJpaRepository;

    @Transactional
    public AnswerResponseDto createAnswer(AnswerRequestDto dto) {
        Long questionId = dto.getQuestionId();
        String content = dto.getContent();
        String username = dto.getAuthorUsername();
        Optional<Question> question = questionJpaRepository.findById(questionId);
        Question findQuestion = question.orElseThrow(() -> new RuntimeException("Not Found, Bad Request"));

        Optional<User> author = userJpaRepository.findByUsername(username);
        User findAuthor = author.orElseThrow(() -> new RuntimeException("Not Found, Bad Request"));
        Answer newAnswer = Answer.createAnswer(content);
        findQuestion.addAnswer(newAnswer);
        findAuthor.addAnswer(newAnswer);

        Answer saveAnswer = answerJpaRepository.save(newAnswer);
        return AnswerResponseDto.of(saveAnswer);
    }

    @Transactional
    public AnswerResponseDto modifyAnswer(Long answerId, String content) {
        Answer findAnswer = answerJpaRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Not Found, Bad Request"));
        findAnswer.setContent(content);
        Answer saveAnswer = answerJpaRepository.save(findAnswer);
        return AnswerResponseDto.of(saveAnswer);
    }

    public AnswerResponseDto findAnswer(Long answerId) {
        Optional<Answer> answer = answerJpaRepository.findById(answerId);
        Answer findAnswer = answer.orElseThrow(() -> new RuntimeException("Not Found, Bad Request"));
        return AnswerResponseDto.of(findAnswer);
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        answerJpaRepository.deleteById(answerId);
    }

    @Transactional
    public void voteQuestion(Long answerId, String username) {
        User findUser = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found, Bad Request"));
        Answer findAnswer = answerJpaRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("User Not Found, Bad Request"));
        findAnswer.getVoter().add(findUser);
        answerJpaRepository.save(findAnswer);
    }
}
