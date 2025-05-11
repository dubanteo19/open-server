package com.nonglam.open_server.domain.post;

import com.nonglam.open_server.domain.comment.Comment;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.shared.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @ManyToOne()
  Opener author;
  @Column(nullable = false, length = 400)
  String content;
  @OneToMany(mappedBy = "post")
  List<Comment> comments = new ArrayList<>();

  @PrePersist
  public void onCreated() {
    deleted = false;
  }

  int sentiment = -1;
  @ManyToMany(mappedBy = "likedPosts")
  Set<Opener> likedByOpeners = new HashSet<>();

  @ManyToMany(mappedBy = "bookmarkedPosts")
  Set<Opener> bookmarkedByOpeners = new HashSet<>();
  boolean deleted;

  int viewCount = 0;
  int likeCount = 0;
  int commentCount = 0;

}
