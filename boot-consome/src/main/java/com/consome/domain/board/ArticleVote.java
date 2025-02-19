package com.consome.domain.board;

import com.consome.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ArticleVote",
        uniqueConstraints = @UniqueConstraint(name = "uniq_article_user", columnNames = {"article_id", "user_id"}),
        indexes = {
                @Index(name = "idx_article_id", columnList = "article_id"),
                @Index(name = "idx_user_id", columnList = "user_id")
        }
)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Article와의 ManyToOne 관계
    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    // User 엔티티와의 ManyToOne 관계 (투표자)
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private VoteStatus vote;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}