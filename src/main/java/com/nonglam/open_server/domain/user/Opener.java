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

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Entity
@DiscriminatorValue("OPENER")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Opener extends User {
  @OneToMany(mappedBy = "author")
  List<Post> posts = new ArrayList<>();
  @OneToMany(mappedBy = "author")
  List<Comment> comments = new ArrayList<>();
  @OneToMany(mappedBy = "opener")
  List<Notification> notifications = new ArrayList<>();
  @Column(length = 500)
  String bio = "No bio";
  boolean verified;
  String location = "VietNam";
  int spamFlagCount;
  @CreatedDate
  @Column(name = "join_date", updatable = false)
  LocalDateTime joinDate;
  @ManyToMany
  @JoinTable(name = "opener_liked_posts", joinColumns = @JoinColumn(name = "opener_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
  Set<Post> likedPosts = new HashSet<>();
  @ManyToMany
  @JoinTable(name = "opener_bookmarked_posts", joinColumns = @JoinColumn(name = "opener_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
  Set<Post> bookmarkedPosts = new HashSet<>();
  @ManyToMany
  @JoinTable(name = "opener_following", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
  Set<Opener> following = new HashSet<>();
  @ManyToMany(mappedBy = "following")
  Set<Opener> followers = new HashSet<>();

  public void flagAsSpammer() {
    this.spamFlagCount++;
  }
}
