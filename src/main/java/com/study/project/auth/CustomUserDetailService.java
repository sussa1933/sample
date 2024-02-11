package com.study.project.auth;

import com.study.project.user.entity.User;
import com.study.project.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    private final UserRoleUtils userRoleUtils;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을수 없습니다."));
        List<GrantedAuthority> authorities = setAuthenticationToContext(findUser.getRoles());
        log.info("#### CustomUserDetailService loadUserByUsername()");
        return new org.springframework.security.core.userdetails.User(findUser.getUsername(), findUser.getPassword(), authorities);
    }


    private List<GrantedAuthority> setAuthenticationToContext(List<String> rolesList) {
        log.info("### SecurityContextHolder SetAuthentication = {}", rolesList );
        return userRoleUtils.createAuthorities(rolesList);
    }
}
