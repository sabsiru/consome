package com.consome.service;


import com.consome.domain.CurrentPoint;
import com.consome.domain.PointHistory;
import com.consome.domain.User;
import com.consome.repository.CurrentPointRepository;
import com.consome.repository.PointHistoryRepository;
import com.consome.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CurrentPointRepository currentPointRepository;

    @Transactional
    public void updatePoint(Long userId, int changeAmount, String reason) {

        // User 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // CurrentPoint 조회 또는 초기화
        CurrentPoint currentPoint = currentPointRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재 하지 않습니다." + userId));

        // 포인트 계산
        int newPoint = currentPoint.getCurrentPoint() + changeAmount;
        if (newPoint < 0) {
            throw new IllegalArgumentException("포인트가 부족하여 차감할 수 없습니다.");
        }

        // CurrentPoint 업데이트
        currentPoint.updateCurrentPoint(user,newPoint);
        currentPointRepository.save(currentPoint);

        // PointHistory 기록
        PointHistory pointHistory = PointHistory.createPointHistory(user, changeAmount, newPoint, reason);
        pointHistoryRepository.save(pointHistory);
    }
}
