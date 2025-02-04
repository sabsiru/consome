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

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CurrentPointRepository currentPointRepository;

    // 회원가입
    public Long register(User user) {
        //중복검증
        validateDuplicateUser(user);
        userRepository.save(user);

        CurrentPoint currentPoint = CurrentPoint.initCurrentPoint(user);
        PointHistory pointHistory = PointHistory.createInitPointHistory(user);
        currentPointRepository.save(currentPoint);
        pointHistoryRepository.save(pointHistory);

        return user.getId();
    }

    //로그인

    // 중복 검사
    private void validateDuplicateUser(User user) {
        if (userRepository.findByLoginId(user.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID입니다.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.findByNickname(user.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        if( userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
        }
    }

    /**
     * 회원 전체 조회
     *
     * @return 모든 사용자 리스트
     */
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 아이디로 회원 단건 조회
     *
     * @param id 사용자 ID
     * @return 사용자 객체
     * @throws IllegalArgumentException 사용자가 존재하지 않을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
    }
    /**
     * 전화번호로 아이디 찾기
     *
     * @param phoneNumber
     * @return 사용자ID
     * @throws IllegalArgumentException 사용자가 존재하지 않을 경우 예외 발생
     * */
    @Transactional(readOnly = true)
    public String findLoginIdByPhoneNumber(String phoneNumber) {
        return userRepository.findLoginIdByPhoneNumber(phoneNumber)
                .orElseThrow(()-> new IllegalArgumentException("해당 전화번호로 가입된 ID가 없습니다."));
    }

    /**
     * 이메일로 아이디 찾기
     *
     * @param email
     * @return 사용자ID
     * @throws IllegalArgumentException 사용자가 존재하지 않을 경우 예외 발생
     * */
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("해당 이메일로 가입된 ID가 없습니다."));
    }
}