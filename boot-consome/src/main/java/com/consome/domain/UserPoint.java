package com.consome.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 포인트 ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User 엔티티 참조

    private int totalPoints; // 현재 총 포인트

    private LocalDateTime updatedAt; // 마지막 업데이트 시간

    // 생성 메서드
    public static UserPoint create(User user) {
        UserPoint userPoint = new UserPoint();
        userPoint.setUser(user); // 연관 관계 설정
        userPoint.totalPoints = 0;
        userPoint.updatedAt = LocalDateTime.now();
        return userPoint;
    }

    // 연관 관계 설정 메서드
    public void setUser(User user) {
        this.user = user;
    }

    // 포인트 추가
    public void addPoints(int points) {
        validatePoints(points);
        this.totalPoints += points;
        updateTimestamp();
    }

    // 포인트 차감
    public void deductPoints(int points) {
        validatePoints(points);
        if (this.totalPoints < points) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }
        this.totalPoints -= points;
        updateTimestamp();
    }

    // private 메서드: 포인트 검증
    private void validatePoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("포인트는 음수일 수 없습니다.");
        }
    }

    // private 메서드: 타임스탬프 업데이트
    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}