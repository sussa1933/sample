package com.study.project.user.repository;

import com.study.project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User,Long> {
}
