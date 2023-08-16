package com.study.project.question.controller;

import com.study.project.answer.dto.AnswerForm;
import com.study.project.exception.ErrorCode;
import com.study.project.exception.ServiceLogicException;
import com.study.project.question.dto.QuestionForm;
import com.study.project.question.dto.QuestionRequestDto;
import com.study.project.question.dto.QuestionResponseDto;
import com.study.project.question.dto.QuestionSimpleResponseDto;
import com.study.project.question.service.QuestionService;
import com.study.project.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final UserService userService;

    private final QuestionService questionService;

    @GetMapping("/list")
    public ModelAndView listQuestion(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw
    ) {
        Page<QuestionSimpleResponseDto> questionList = questionService.findQuestionList(page, kw);
        return new ModelAndView(
                "question_list",
                Map.of(
                        "paging", questionList,
                        "kw", kw
                )
        );
    }

    @GetMapping("/create")
    public ModelAndView createQuestionForm() {
        QuestionForm questionForm = new QuestionForm();
        return new ModelAndView(
                "question_form",
                Map.of(
                        "questionForm", questionForm
                )
        );
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(
            @PathVariable("id") Long id
    ) {
        QuestionResponseDto question = questionService.findQuestionDetail(id);
        AnswerForm answerForm = new AnswerForm();
        return new ModelAndView(
                "question_detail",
                Map.of(
                        "question", question,
                        "answerForm", answerForm
                )
        );

    }

    @PostMapping("/create")
    public ModelAndView createQuestion(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView(
                    "question_form"
            );
        } else if (principal == null) {
            return new ModelAndView(
                    "login_form"
            );
        }
        QuestionRequestDto requestDto = QuestionRequestDto.builder()
                .authorUsername(principal.getName())
                .subject(questionForm.getSubject())
                .content(questionForm.getContent())
                .build();
        questionService.createQuestion(requestDto);
        return new ModelAndView(
                "redirect:/question/list"
        );
    }

    @GetMapping("/modify/{id}")
    public ModelAndView questionModify(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        QuestionResponseDto question = questionService.findQuestionDetail(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ServiceLogicException(ErrorCode.ACCESS_DENIED);
        }
        QuestionForm questionForm = new QuestionForm();
        questionForm.setContent(question.getContent());
        questionForm.setSubject(question.getSubject());

        return new ModelAndView(
                "question_modify_form",
                Map.of(
                        "question", question,
                        "questionForm", questionForm,
                        "questionId", id
                )
        );
    }

    @PostMapping("/modify/{id}")
    public ModelAndView questionModify(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal,
            @PathVariable("id") Long id
    ) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView(
                    "question_form"
            );
        } else if (principal == null) {
            return new ModelAndView(
                    "login_form"
            );
        }
        QuestionRequestDto requestDto = QuestionRequestDto.builder()
                .content(questionForm.getContent())
                .subject(questionForm.getSubject())
                .authorUsername(principal.getName())
                .build();
        questionService.modifyQuestion(id, requestDto);
        return new ModelAndView(
                String.format("redirect:/question/detail/%s", id)
        );
    }

    @GetMapping("/delete/{id}")
    public ModelAndView questionDelete(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        if (principal == null) {
            return new ModelAndView(
                    "login_form"
            );
        }
        questionService.deleteQuestion(id, principal.getName());
        return new ModelAndView(
                "redirect:/"
        );
    }

    @GetMapping("/vote/{id}")
    public ModelAndView questionVote(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        if (principal == null) {
            return new ModelAndView(
                    "login_form"
            );
        }
        questionService.voteQuestion(id, principal.getName());
        return new ModelAndView(
                String.format("redirect:/question/detail/%s", id)
        );
    }
}
