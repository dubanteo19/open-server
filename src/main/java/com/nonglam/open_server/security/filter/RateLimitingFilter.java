package com.nonglam.open_server.security.filter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Predicate;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nonglam.open_server.shared.ratelimiter.RateLimiterService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
@Log4j2
public class RateLimitingFilter extends OncePerRequestFilter {
  RateLimiterService rateLimiterService;
  private final static String COOKIE_NAME = "client_id";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    String clientId = getOrSetClientIdCookie(request, response);
    if (!rateLimiterService.isAllow(clientId)) {
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.getWriter().write("Too many request. try later");
      log.debug("too many request");
      return;
    }

    filterChain.doFilter(request, response);
  }

  private String getOrSetClientIdCookie(HttpServletRequest request, HttpServletResponse response) {
    String clientId = null;
    var cookies = request.getCookies();
    Predicate<Cookie> filterClientId = (cookie -> cookie.getName().equals(COOKIE_NAME));
    if (cookies != null) {
      clientId = Arrays.stream(cookies)
          .filter(filterClientId)
          .map(Cookie::getValue)
          .findFirst().orElse(null);
    }
    if (clientId == null) {
      clientId = UUID.randomUUID().toString();
      var cookie = new Cookie(COOKIE_NAME, clientId);
      cookie.setPath("/");
      cookie.setHttpOnly(true);
      int sevenDaysInSeconds = (int) Duration.ofDays(7).getSeconds();
      cookie.setMaxAge(sevenDaysInSeconds);
      cookie.setSecure(false);
      response.addCookie(cookie);
    }
    return clientId;
  }

}
