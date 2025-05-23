package com.nonglam.open_server.domain.auth.dto;

import com.nonglam.open_server.domain.auth.dto.request.RegisterRequest;
import com.nonglam.open_server.domain.auth.dto.response.UserResponse;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public Opener toOpener(RegisterRequest request) {
    var opener = new Opener();
    opener.setEmail(request.email());
    opener.setDisplayName(request.displayName());
    opener.setUsername(request.username());
    return opener;
  }

  public UserResponse toUserResponse(User user) {
    return new UserResponse(user.getId(), user.getUsername(), user.getDisplayName(), user.getAvatarUrl());

  }

}
