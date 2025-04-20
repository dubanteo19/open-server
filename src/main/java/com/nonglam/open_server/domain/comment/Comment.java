package com.nonglam.open_server.domain.comment;


import com.nonglam.open_server.domain.post.Post;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.shared.Auditable;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200)
    private String content;
    @ManyToOne
    private Opener author;
    @ManyToOne
    private Post post;
}
