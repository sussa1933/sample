package com.study.project.question.entity;


import com.study.project.answer.entity.Answer;
import com.study.project.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String subject;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private User author;

    @ToString.Exclude
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private Set<Answer> answerList = new LinkedHashSet<>();

    @ManyToMany
    Set<User> voter = new LinkedHashSet<>();

    public static Question createQuestion(
            String subject,
            String content
    ) {
        return Question.builder()
                .subject(subject)
                .content(content)
                .build();
    }

    public void addAuthor(User author) {
        this.author = author;
        author.addQuestion(this);
    }


}
