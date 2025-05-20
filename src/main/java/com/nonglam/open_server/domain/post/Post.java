package com.nonglam.open_server.domain.post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nonglam.open_server.domain.comment.Comment;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.shared.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

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

  int sentiment = -1;
  @ManyToMany(mappedBy = "likedPosts")
  Set<Opener> likedByOpeners = new HashSet<>();

  @ManyToMany(mappedBy = "bookmarkedPosts")
  Set<Opener> bookmarkedByOpeners = new HashSet<>();

  boolean deleted = false;
  long simHash;
  int viewCount = 0;
  int likeCount = 0;
  int commentCount = 0;

}
