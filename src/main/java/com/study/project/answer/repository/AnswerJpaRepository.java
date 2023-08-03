package com.study.project.answer.repository;

import com.study.project.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerJpaRepository extends JpaRepository<Answer, Long> {
}
