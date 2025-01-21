package com.consome.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CurrentPoint {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int currentPoint;

    private LocalDateTime updatedAt;

    public static CurrentPoint createPoint(User user, int currentPoint) {
        CurrentPoint point = new CurrentPoint();
        point.user= user;
        point.currentPoint = currentPoint;
        point.updatedAt = LocalDateTime.now();
        return point;
    }

    public void updateCurrentPoint(User user, int currentPoint) {
        this.user = user;
        this.currentPoint = currentPoint;
        this.updatedAt = LocalDateTime.now();
    }

    //회원가입시 기본값 설정
    public static CurrentPoint initCurrentPoint(User user) {
        return createPoint(user, 100);
    }
}
