package com.study.project.user.service;

import com.study.project.user.dto.UserRequestDto;
import com.study.project.user.entity.User;
import com.study.project.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;

    public User createUser(UserRequestDto dto) {
        String email = dto.getEmail();
        String username = dto.getUsername();
        String password = dto.getPassword1();
        User user = User.createUser(email, username, password);
        User save = userJpaRepository.save(user);
        return save;
    }


}
