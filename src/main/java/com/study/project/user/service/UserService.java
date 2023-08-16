package com.study.project.user.service;

import com.study.project.exception.ErrorCode;
import com.study.project.exception.ServiceLogicException;
import com.study.project.user.dto.UserRequestDto;
import com.study.project.user.dto.UserResponseDto;
import com.study.project.user.entity.User;
import com.study.project.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;

    private final PasswordEncoder passwordEncoder;

    public UserResponseDto createUser(UserRequestDto dto) {
        String email = dto.getEmail();
        String username = dto.getUsername();
        String password = dto.getPassword();
        verifyUser(username);
        String encodePassword = passwordEncoder.encode(password);
        User user = User.createUser(email, username, encodePassword);
        User saveUser = userJpaRepository.save(user);
        return UserResponseDto.of(saveUser);
    }

    public UserResponseDto findUser(String username) {
        User findUser = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found, Bad Request"));
        return UserResponseDto.of(findUser);
    }

    public void verifyUser(String username) {
        Optional<User> user = userJpaRepository.findByUsername(username);
        if (user.isPresent()) {
            throw new ServiceLogicException(ErrorCode.USER_EXIST);
        }
    }

}
