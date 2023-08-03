package com.study.project.question.entity;


import com.study.project.answer.entity.Answer;
import com.study.project.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String subject;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private User author;

    @ToString.Exclude
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private Set<Answer> answerList = new LinkedHashSet<>();

    @ManyToMany
    private Set<User> voter = new LinkedHashSet<>();

}
