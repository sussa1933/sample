package com.study.project.auth;

import com.study.project.user.entity.User;
import com.study.project.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을수 없습니다."));
        log.info("#### CustomUserDetailService loadUserByUsername()");
        return new UserDetail(findUser);
    }

    private final class UserDetail extends User implements UserDetails {

        public UserDetail(User user) {
            setId(user.getId());
            setUsername(user.getUsername());
            setPassword(user.getPassword());
            setRoles(user.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
