package com.nonglam.open_server.domain.post;

import com.nonglam.open_server.domain.comment.Comment;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.shared.Auditable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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

    private boolean deleted;

    private int viewCount = 0;
    private int likeCount = 0;
    private int commentCount = 0;
}
