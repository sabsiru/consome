package com.consome.domain.board;

import com.consome.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Article", indexes = {
        @Index(name = "idx_board_id", columnList = "board_id"),
        @Index(name = "idx_author_id", columnList = "author_id"),
        @Index(name = "idx_category_id", columnList = "category_id")
})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Board와의 관계
    @ManyToOne(optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    // 작성자(User)와의 관계
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Category와의 ManyToOne 관계 (게시글이 속한 카테고리)
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    @Builder.Default
    private Integer views = 0;

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