package com.nonglam.open_server.domain.notification;

import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.domain.user.User;
import com.nonglam.open_server.shared.Auditable;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Notification extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Opener opener;
    @Column(nullable = false, length = 200)
    private String content;
    @Column(nullable = false)
    private String title;
}
