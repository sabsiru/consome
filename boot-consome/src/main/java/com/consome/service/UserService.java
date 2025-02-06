package com.consome.service;

import com.consome.domain.CurrentPoint;
import com.consome.domain.PointHistory;
import com.consome.domain.User;
import com.consome.repository.CurrentPointRepository;
import com.consome.repository.PointHistoryRepository;
import com.consome.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;



    // 회원가입
    @Transactional
    public User register(User user) {
        //입력검증
        validateUser(user);

        //중복검증
        validateDuplicateUser(user);

        //암호화 및 휴대폰번호 포맷 적용
        User createUser = User.createUser(user.getLoginId(),
                user.getNickname(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                passwordEncoder);

       userRepository.save(createUser);

        CurrentPoint currentPoint = CurrentPoint.initCurrentPoint(createUser);
        PointHistory pointHistory = PointHistory.createInitPointHistory(createUser);

        currentPointRepository.save(currentPoint);
        pointHistoryRepository.save(pointHistory);

        return createUser;
    }

    //로그인
    public User login(User user) {

        return user;
    }
    //입력검증
    private void validateUser(User user) {
        if(user.getLoginId().isEmpty()) throw new IllegalArgumentException("로그인 ID를 입력해 주세요.");
        if(user.getNickname().isEmpty()) throw new IllegalArgumentException("닉네임을 입력해 주세요.");
        if(user.getName().isEmpty()) throw new IllegalArgumentException("이름을 입력해 주세요.");
        if(user.getEmail().isEmpty()) throw new IllegalArgumentException("이메일을 입력해 주세요.");
        if(user.getPhoneNumber().isEmpty()) throw new IllegalArgumentException("전화번호를 입력해 주세요.");
        if(user.getPassword().isEmpty()) throw new IllegalArgumentException("비밀번호를 입력해 주세요.");
    }

    // 중복검증
    private void validateDuplicateUser(User user) {
        if (userRepository.findByLoginId(user.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID입니다.");
        }
        if (userRepository.findByNickname(user.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
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