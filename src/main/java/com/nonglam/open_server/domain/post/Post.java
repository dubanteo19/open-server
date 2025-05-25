package com.nonglam.open_server.domain.post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nonglam.open_server.domain.comment.Comment;
import com.nonglam.open_server.domain.postbookmark.PostBookmark;
import com.nonglam.open_server.domain.postlike.PostLike;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.shared.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(callSuper = false)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private Opener author;
  @Column(nullable = false, length = 400)
  private String content;
  @OneToMany(mappedBy = "post")
  private List<Comment> comments = new ArrayList<>();
  private int sentiment = -1;
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PostLike> postLikes = new HashSet<>();
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PostBookmark> postBookmarks = new HashSet<>();
  private boolean deleted = false;
  private long simHash;
  private int viewCount = 0;
  private int likeCount = 0;
  private int commentCount = 0;

  public void updateContent(String newContent) {
    this.content = newContent;
  }

  public void markAsDeleted() {
    this.deleted = true;
  }

  public Post(String content, Opener author, long simHash) {
    this.content = content;
    this.author = author;
    this.simHash = simHash;
  }

  public void updateSentitment(int newSentitment) {
    this.sentiment = newSentitment;
  }

  public boolean isAuthor(Opener opener) {
    return author.getId() == opener.getId();
  }

}
