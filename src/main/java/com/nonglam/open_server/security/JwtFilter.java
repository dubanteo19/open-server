package com.nonglam.open_server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nonglam.open_server.exception.ApiException;
import com.nonglam.open_server.shared.ErrorCode;

import java.io.IOException;

@Component

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  JwtUtil jwtUtil;
  CustomUserDetailService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String email;
    final String token;
    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      filterChain.doFilter(request, response);
      return;
    }
    token = authHeader.substring(7);
    email = jwtUtil.extractEmail(token);
    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      var userDetail = (CustomUserDetail) userDetailsService.loadUserByUsername(email);

      if (userDetail.getUser().isBlocked()) {
        throw new ApiException("Your account is blocked", ErrorCode.OPENER_BLOCKED);
      }
      if (jwtUtil.isTokenValid(token, false)) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(userDetail,
            null, userDetail.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
