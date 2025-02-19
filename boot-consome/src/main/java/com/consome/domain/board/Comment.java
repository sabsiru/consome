package com.consome.domain.board;

import com.consome.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Comment", indexes = {
        @Index(name = "idx_article_id", columnList = "article_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_parent_id", columnList = "parent_id")
})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Article와의 ManyToOne 관계
    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    // User 엔티티와의 ManyToOne 관계 (댓글 작성자)
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 자기 참조: 부모 댓글 (최상위 댓글은 null)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private Integer likes = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer dislikes = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "is_active", nullable = false, length = 20)
    private ActiveStatus isActive = ActiveStatus.ACTIVE;
}