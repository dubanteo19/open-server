package com.nonglam.open_server.security;

import com.nonglam.open_server.domain.auth.AuthRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomUserDetailService implements UserDetailsService {
    AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = authRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new CustomUserDetail(user);
    }
}
