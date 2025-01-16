package com.consome.service;

import com.consome.domain.User;
import com.consome.domain.UserPoint;
import com.consome.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public Long register(User user) {
        validateDuplicateUser(user);

        //전화번호 포맷
        String formatPhoneNumber = formatPhoneNumber(user.getPhoneNumber());
        user.setPhoneNumber(formatPhoneNumber);
        user.setRole(User.Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return user.getId();
    }

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
        if( userRepository.findByPhoneNumber(formatPhoneNumber(user.getPhoneNumber())).isPresent()) {
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

    //전화번호 포맷팅
    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 11 || !phoneNumber.matches("\\d{11}")) {
            System.out.println("phoneNumber = " + phoneNumber);
            throw new IllegalArgumentException("전화번호는 숫자 11자리여야 합니다.");
        }
        return phoneNumber.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
    }
}