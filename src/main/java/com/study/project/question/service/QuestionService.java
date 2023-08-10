package com.study.project.question.service;

import com.study.project.question.dto.QuestionRequestDto;
import com.study.project.question.dto.QuestionResponseDto;
import com.study.project.question.dto.QuestionSimpleResponseDto;
import com.study.project.question.entity.Question;
import com.study.project.question.repository.QuestionJpaRepository;
import com.study.project.user.entity.User;
import com.study.project.user.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionJpaRepository questionJpaRepository;

    private final UserJpaRepository userJpaRepository;

    @Transactional
    public QuestionResponseDto createQuestion(QuestionRequestDto dto) {
        String subject = dto.getSubject();
        String content = dto.getContent();
        String username = dto.getAuthorUsername();
        Optional<User> findUser = userJpaRepository.findByUsername(username);
        User user = findUser.orElseThrow(() -> new RuntimeException("Question NOT FOUND, Bad Request"));
        Question question = Question.createQuestion(subject, content);
        question.addAuthor(user);
        Question saveQuestion = questionJpaRepository.save(question);
        return QuestionResponseDto.of(saveQuestion);
    }

    public QuestionResponseDto findQuestionDetail(Long questionId) {
        Optional<Question> question = questionJpaRepository.findById(questionId);
        Question findQuestion = question.orElseThrow(() -> new RuntimeException("Question Not Found,Bad Request"));
        return QuestionResponseDto.of(findQuestion);
    }

    public QuestionSimpleResponseDto findSimpleQuestion(Long questionId) {
        Optional<Question> question = questionJpaRepository.findById(questionId);
        Question findQuestion = question.orElseThrow(() -> new RuntimeException("Question Not Found,Bad Request"));
        return QuestionSimpleResponseDto.of(findQuestion);
    }


    @Transactional
    public Page<QuestionSimpleResponseDto> findQuestionList(int page, String kw) {
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Question> findQuestions = questionJpaRepository
                .findAllBySubjectContainingIgnoreCaseOrContentContainingIgnoreCase(pageRequest, kw, kw);
        List<QuestionSimpleResponseDto> questionList = findQuestions.getContent()
                .stream()
                .map(QuestionSimpleResponseDto::of)
                .toList();
        Page<QuestionSimpleResponseDto> response =
                new PageImpl<>(questionList, pageRequest, findQuestions.getTotalElements());
        return response;
    }

    @Transactional
    public QuestionResponseDto modifyQuestion(Long questionId, QuestionRequestDto dto) {
        Optional<Question> question = questionJpaRepository.findById(questionId);
        Question findQuestion = question.orElseThrow(() -> new RuntimeException("Question Not Found, Bad Request"));
        findQuestion.setContent(dto.getContent());
        findQuestion.setSubject(dto.getSubject());
        Question updateQuestion = questionJpaRepository.save(findQuestion);
        return QuestionResponseDto.of(updateQuestion);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        questionJpaRepository.deleteById(questionId);
    }

    @Transactional
    public void voteQuestion(Long questionId, Long userId) {
        Question findQuestion = questionJpaRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question Not Found, Bad Request"));
        User findUser = userJpaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found, Bad Request"));
        findQuestion.getVoter().add(findUser);
        questionJpaRepository.save(findQuestion);
    }




}
