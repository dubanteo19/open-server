package com.nonglam.open_server.shared;

import java.nio.charset.StandardCharsets;

public class SimHashUtil {
  private static final int HASH_BITS = 64;

  public static long simHash(String content) {
    int[] vector = new int[HASH_BITS];
    String[] tokens = content.toLowerCase().split("\\s+");
    for (String token : tokens) {
      long hash = murmurHash64(token);
      for (int i = 0; i < HASH_BITS; i++) {
        if (((hash >> i) & 1) == 1)
          vector[i] += 1;
        else
          vector[i] -= 1;
      }
    }
    long fingerprint = 0L;
    for (int i = 0; i < HASH_BITS; i++) {
      if (vector[i] >= 0) {
        fingerprint |= (1L << i);
      }
    }
    return fingerprint;
  }

  public static int hammingDistance(long hash1, long hash2) {
    return Long.bitCount(hash1 ^ hash2);
  }

  public static long murmurHash64(String key) {
    byte[] data = key.getBytes(StandardCharsets.UTF_8);
    int length = data.length;
    int seed = 0xe17a1465;
    long m = 0xc6a4a7935bd1e995L;
    int r = 47;
    long h = (seed & 0xffffffffL) ^ (length * m);
    int length8 = length / 8;
    for (int i = 0; i < length8; i++) {
      int i8 = i * 8;
      long k = ((long) data[i8] & 0xff)
          | (((long) data[i8 + 1] & 0xff) << 8)
          | (((long) data[i8 + 2] & 0xff) << 16)
          | (((long) data[i8 + 3] & 0xff) << 24)
          | (((long) data[i8 + 4] & 0xff) << 32)
          | (((long) data[i8 + 5] & 0xff) << 40)
          | (((long) data[i8 + 6] & 0xff) << 48)
          | (((long) data[i8 + 7] & 0xff) << 56);
      k *= m;
      k ^= k >>> r;
      k *= m;
      h ^= k;
      h *= m;
    }
    int rem = length % 8;
    int offset = length & ~7;
    switch (rem) {
      case 7:
        h ^= (long) (data[offset + 6] & 0xff) << 48;
      case 6:
        h ^= (long) (data[offset + 5] & 0xff) << 40;
      case 5:
        h ^= (long) (data[offset + 4] & 0xff) << 32;
      case 4:
        h ^= (long) (data[offset + 3] & 0xff) << 24;
      case 3:
        h ^= (long) (data[offset + 2] & 0xff) << 16;
      case 2:
        h ^= (long) (data[offset + 1] & 0xff) << 8;
      case 1:
        h ^= (long) (data[offset] & 0xff);
        h *= m;
    }
    h ^= h >>> r;
    h *= m;
    h ^= h >>> r;
    return h;
  }

}
