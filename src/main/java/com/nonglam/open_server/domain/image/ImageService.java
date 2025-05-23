package com.nonglam.open_server.domain.image;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageService {
  RestTemplate restTemplate;
  ObjectMapper objectMapper;

  public List<String> getImages(String q) {

    String API_KEY = "50172300-c765daab9e75705590ea5519a";
    String PIXABAY_API_URL = "https://pixabay.com/api/";
    String url = UriComponentsBuilder.fromUriString(PIXABAY_API_URL)
        .queryParam("key", API_KEY)
        .queryParam("q", q)
        .queryParam("image_type", "photo")
        .queryParam("per_page", 10)
        .toUriString();

    String response = restTemplate.getForObject(url, String.class);
    var imagelUrls = new ArrayList<String>();
    try {
      var root = objectMapper.readTree(response);
      var hits = root.path("hits");
      for (var hit : hits) {
        String imageUrl = hit.path("webformatURL").asText();
        imagelUrls.add(imageUrl);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return imagelUrls;

  }

}
