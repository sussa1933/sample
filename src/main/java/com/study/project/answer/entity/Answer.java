package com.study.project.answer.entity;

import com.study.project.question.entity.Question;
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
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private User author;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private Question question;

    @ManyToMany
    Set<User> voter = new LinkedHashSet<>();


}
