package com.study.project.answer.controller;

import com.study.project.answer.dto.AnswerForm;
import com.study.project.answer.dto.AnswerRequestDto;
import com.study.project.answer.dto.AnswerResponseDto;
import com.study.project.answer.service.AnswerService;
import com.study.project.exception.ErrorCode;
import com.study.project.exception.ServiceLogicException;
import com.study.project.question.dto.QuestionResponseDto;
import com.study.project.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;

    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public ModelAndView createAnswer(
            @Valid AnswerForm answerForm,
            BindingResult bindingResult,
            Principal principal,
            @PathVariable("id") Long id
    ) {
        if (bindingResult.hasErrors()) {
            QuestionResponseDto findQuestion = questionService.findQuestionDetail(id);
            return new ModelAndView(
                    "question_detail",
                    Map.of("question", findQuestion)
            );
        } else if (principal == null) {
            return new ModelAndView(
                    "login_form"
            );
        }
        AnswerRequestDto requestDto = AnswerRequestDto.builder()
                .authorUsername(principal.getName())
                .questionId(id)
                .content(answerForm.getContent())
                .build();
        answerService.createAnswer(requestDto);
        return new ModelAndView(
                String.format("redirect:/question/detail/%s", id)
        );
    }

    @GetMapping("/modify/{id}")
    public ModelAndView modifyAnswerForm(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        AnswerResponseDto answer = answerService.findAnswer(id);
        if (principal == null || !principal.getName().equals(answer.getAuthor().getUsername())) {
            throw new ServiceLogicException(ErrorCode.ACCESS_DENIED);
        }
        AnswerForm answerForm = new AnswerForm();
        answerForm.setContent(answer.getContent());
        return new ModelAndView(
                "answer_form",
                Map.of("answerForm", answerForm,
                        "answerId", id)
        );
    }

    @PostMapping("/modify/{id}")
    public ModelAndView modifyAnswer(
            @Valid AnswerForm answerForm,
            BindingResult bindingResult,
            @PathVariable("id") Long id,
            Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView(
                    "answer_form"
            );
        } else if (principal == null) {
            return new ModelAndView(
                    "login_form"
            );
        }
        AnswerResponseDto response =
                answerService.modifyAnswer(id, answerForm.getContent(), principal.getName());
        return new ModelAndView(
                String.format("redirect:/question/detail/%s", response.getQuestionId())
        );
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteAnswer(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        if (principal == null) {
            return new ModelAndView(
                    "login_form"
            );
        }
        Long questionId = answerService.deleteAnswer(id, principal.getName());
        return new ModelAndView(
                String.format("redirect:/question/detail/%s", questionId)
        );
    }

    @GetMapping("/vote/{id}")
    public ModelAndView voteAnswer(
            Principal principal,
            @PathVariable("id") Long id
    ) {
        if (principal == null) {
            return new ModelAndView(
                    "login_form"
            );
        }
        Long questionId = answerService.voteAnswer(id, principal.getName());
        return new ModelAndView(
                String.format("redirect:/question/detail/%s", questionId)
        );
    }
}
