package com.nonglam.open_server.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.nonglam.open_server.domain.comment.Comment;
import com.nonglam.open_server.domain.notification.Notification;
import com.nonglam.open_server.domain.post.Post;
import com.nonglam.open_server.domain.postbookmark.PostBookmark;
import com.nonglam.open_server.domain.postlike.PostLike;
import com.nonglam.open_server.domain.userfollow.OpenerFollow;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class)
@Entity
@DiscriminatorValue("OPENER")
@EqualsAndHashCode(callSuper = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Opener extends User {
  @OneToMany(mappedBy = "author")
  private List<Post> posts = new ArrayList<>();
  @OneToMany(mappedBy = "author")
  private List<Comment> comments = new ArrayList<>();
  @OneToMany(mappedBy = "opener")
  private List<Notification> notifications = new ArrayList<>();
  @Column(length = 500)
  private String bio = "No bio";
  private boolean verified;
  private String location = "VietNam";
  private int spamFlagCount;
  @CreatedDate
  @Column(name = "join_date", updatable = false)
  private LocalDateTime joinDate;
  @OneToMany(mappedBy = "opener", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PostLike> postLikes = new HashSet<>();
  @OneToMany(mappedBy = "opener", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PostBookmark> postBookmakrs = new HashSet<>();
  @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OpenerFollow> following = new HashSet<>();
  @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OpenerFollow> followers = new HashSet<>();

  public void updateInfo(String bio, String displayName, String location) {
    this.bio = bio;
    super.setDisplayName(displayName);
    this.location = location;
  }

  public void flagAsSpammer() {
    this.spamFlagCount++;
  }

  public void addNotification(Notification notification) {
    this.notifications.add(notification);
    notification.setOpener(this);
  }
}
