package com.nonglam.open_server.domain.postbookmark;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.nonglam.open_server.domain.post.Post;
import com.nonglam.open_server.domain.user.Opener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "post_bookmarks", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "opener_id", "post_id" })
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostBookmark {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;
  @ManyToOne
  @JoinColumn(name = "opener_id", nullable = false)
  private Opener opener;
  @CreatedDate
  @Column(name = "bookmarked_at", updatable = false)
  private LocalDate bookmarkedAt;
}
