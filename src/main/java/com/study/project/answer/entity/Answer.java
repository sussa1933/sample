package com.study.project.answer.entity;

import com.study.project.audit.Auditable;
import com.study.project.question.entity.Question;
import com.study.project.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "answers")
public class Answer extends Auditable {

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

    public static Answer createAnswer(String content) {
        return Answer.builder()
                .content(content)
                .voter(new LinkedHashSet<>())
                .build();
    }

    public void addQuestion(Question question) {
        this.question = question;
    }

    public void addAuthor(User user) {
        this.author = user;
    }


}
