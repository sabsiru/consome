package com.consome.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 포인트 변동 내역 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user; // User와 다대일 관계

    private int changeAmount; // 포인트 변동량 (+/-)
    private int currentPoint; // 현재 포인트
    private String reason; // 포인트 변경 이유
    private LocalDateTime createdAt; // 변동 시각

    // 정적 팩토리 메서드
    public static PointHistory createPointHistory(User user, int changeAmount,int currentPoint, String reason) {
        PointHistory pointHistory = new PointHistory();
        pointHistory.user = user;
        pointHistory.changeAmount = changeAmount;
        pointHistory.currentPoint = currentPoint;
        pointHistory.reason = reason;
        pointHistory.createdAt = LocalDateTime.now();
        return pointHistory;
    }

    //회원가입시 기본값 설정
    public static PointHistory createInitPointHistory(User user) {
        return createPointHistory(user,100,100,"회원가입");
    }

}
