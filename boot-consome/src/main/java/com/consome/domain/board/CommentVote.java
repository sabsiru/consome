package com.consome.domain.board;

import com.consome.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CommentVote",
        uniqueConstraints = @UniqueConstraint(name = "uniq_comment_user", columnNames = {"comment_id", "user_id"}),
        indexes = {
                @Index(name = "idx_comment_id", columnList = "comment_id"),
                @Index(name = "idx_user_id", columnList = "user_id")
        }
)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Comment와의 ManyToOne 관계
    @ManyToOne(optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

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