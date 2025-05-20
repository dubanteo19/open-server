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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostRateLimiterService {
  Map<Long, Deque<Long>> userPostTimestamps = new ConcurrentHashMap<>();
  static int LIMIT = 8;
  static long TIME_WINDOW_MILLIS = 60 * 1000;

  public boolean isAllow(Long openerId) {
    long now = Instant.now().toEpochMilli();
    userPostTimestamps.putIfAbsent(openerId, new ArrayDeque<>());
    var timestamps = userPostTimestamps.get(openerId);
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
