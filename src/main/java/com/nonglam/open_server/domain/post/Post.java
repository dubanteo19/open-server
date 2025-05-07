package com.nonglam.open_server.domain.post;

import com.nonglam.open_server.domain.comment.Comment;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.shared.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Post extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne()
  private Opener author;
  @Column(nullable = false, length = 400)
  private String content;
  @OneToMany(mappedBy = "post")
  private List<Comment> comments = new ArrayList<>();

  @PrePersist
  public void onCreated() {
    deleted = false;
  }

  @ManyToMany(mappedBy = "likedPosts")
  private Set<Opener> likedByOpeners = new HashSet<>();

  @ManyToMany(mappedBy = "bookmarkedPosts")
  private Set<Opener> bookmarkedByOpeners = new HashSet<>();
  private boolean deleted;

  private int viewCount = 0;
  private int likeCount = 0;
  private int commentCount = 0;

  public void viewPost() {
    this.viewCount++;
  }
}
