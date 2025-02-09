package com.consome.service;

import com.consome.auth.JwtUtil;
import com.consome.domain.CurrentPoint;
import com.consome.domain.PointHistory;
import com.consome.domain.User;
import com.consome.dto.request.LoginRequest;
import com.consome.dto.response.UserResponse;
import com.consome.repository.CurrentPointRepository;
import com.consome.repository.PointHistoryRepository;
import com.consome.repository.User.UserRepository;
import com.consome.repository.User.dsl.UserRepositoryDsl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CurrentPointRepository currentPointRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryDsl userRepositoryDsl;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public User register(User user,String ip) {

        //입력 및 중복검증
        validateDuplicateUser(user);

//        // ✅ 같은 IP에서 24시간 내 가입된 사용자 확인
//        if (userRepositoryDsl.existsRecentSignupByIp(ip)) {
//            throw new IllegalArgumentException("동일 IP에서 24시간 내 가입이 감지되었습니다. 일정 시간이 지난 후 다시 시도하세요.");
//        }

        //비밀번호 암호화
        User createUser = User.createUser(user.getLoginId(),
                user.getNickname(),
                user.getEmail(),
                user.getPassword(),
                passwordEncoder,ip);

       userRepository.save(createUser);

        CurrentPoint currentPoint = CurrentPoint.initCurrentPoint(createUser);
        PointHistory pointHistory = PointHistory.createInitPointHistory(createUser);

        currentPointRepository.save(currentPoint);
        pointHistoryRepository.save(pointHistory);

        return createUser;
    }

    //로그인
    public UserResponse login(LoginRequest request) {
        // ✅ 아이디로 먼저 사용자 조회
        Optional<User> userOptional = userRepository.findByLoginId(request.getLoginId());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("아이디를 확인해 주세요.");
        }

        User user = userOptional.get();

        // ✅ 입력한 비밀번호와 저장된 해시 값 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // ✅ JWT 생성
        String accessToken = jwtUtil.generateAccessToken(user.getLoginId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getLoginId());

        // ✅ 유저 정보 반환 (토큰 포함)
        return UserResponse.fromEntity(user, accessToken,refreshToken);
    }

    // 입력 및 중복검증
    private void validateDuplicateUser(User user) {
        if (userRepository.findByLoginId(user.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID입니다.");
        }
        if(user.getLoginId().isEmpty()) throw new IllegalArgumentException("로그인 ID를 입력해 주세요.");

        if (userRepository.findByNickname(user.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        if(user.getNickname().isEmpty()) throw new IllegalArgumentException("닉네임을 입력해 주세요.");
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if(user.getEmail().isEmpty()) throw new IllegalArgumentException("이메일을 입력해 주세요.");
        if(user.getPassword().isEmpty()) throw new IllegalArgumentException("비밀번호를 입력해 주세요.");
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