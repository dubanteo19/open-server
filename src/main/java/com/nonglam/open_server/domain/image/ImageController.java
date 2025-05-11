package com.nonglam.open_server.domain.image;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("api/v1/images")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ImageController {
  ImageService imageService;

  @GetMapping
  public ResponseEntity<APIResponse<List<String>>> getImages(@RequestParam(name = "q") String q) {
    var response = imageService.getImages(q);
    return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("deleted post", response));
  }
}
