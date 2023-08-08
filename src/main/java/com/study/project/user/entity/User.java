package com.study.project.user.entity;

import com.study.project.answer.entity.Answer;
import com.study.project.audit.Auditable;
import com.study.project.question.entity.Question;
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
@Table(name = "users")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 300, nullable = false)
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private Set<Question> questionList = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private Set<Answer> answerList = new LinkedHashSet<>();

    public static User createUser(String email, String username, String password) {
        User user = User.builder()
                .email(email)
                .username(username)
                .password(password)
                .questionList(new LinkedHashSet<>())
                .answerList(new LinkedHashSet<>())
                .build();
        return user;
    }

    public void addQuestion(Question question) {
        questionList.add(question);
    }

}
