package com.nonglam.open_server.shared.ratelimiter;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateLimiterService {
  final Map<String, Deque<Long>> userPostTimestamps = new ConcurrentHashMap<>();
  static int LIMIT = 60;
  static long TIME_WINDOW_MILLIS = 60 * 1000;

  public boolean isAllow(String clientId) {
    long now = Instant.now().toEpochMilli();
    userPostTimestamps.putIfAbsent(clientId, new ArrayDeque<>());
    var timestamps = userPostTimestamps.get(clientId);
    synchronized (timestamps) {
      while (!timestamps.isEmpty() && now - timestamps.peekFirst() > TIME_WINDOW_MILLIS) {
        timestamps.pollFirst();
      }
      if (timestamps.size() < LIMIT) {
        timestamps.addLast(now);
        return true;
      }
      return false;
    }
  }
}
