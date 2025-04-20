package com.nonglam.open_server.domain.user;

import com.nonglam.open_server.domain.comment.Comment;
import com.nonglam.open_server.domain.notification.Notification;
import com.nonglam.open_server.domain.post.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("OPENER")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Opener extends User {
    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "opener")
    private List<Notification> notifications = new ArrayList<>();
    @Column(length = 500)
    private String bio = "No bio";
    private boolean verified = false;
    private String location = "VietNam";
    @CreatedDate
    @Column(name = "join_date", updatable = false)
    private LocalDateTime joinDate;
    @ManyToMany
    @JoinTable(
            name = "opener_liked_posts",
            joinColumns = @JoinColumn(name = "opener_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likedPosts = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "opener_bookmarked_posts",
            joinColumns = @JoinColumn(name = "opener_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> bookmarkedPosts = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "opener_following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns =
            @JoinColumn(name = "following_id")
    )
    private Set<Opener> following = new HashSet<>();
    @ManyToMany(mappedBy = "following")
    private Set<Opener> followers = new HashSet<>();
}
