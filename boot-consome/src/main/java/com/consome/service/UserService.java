package com.consome.service;

import com.consome.auth.JwtUtil;
import com.consome.domain.CurrentPoint;
import com.consome.domain.PointHistory;
import com.consome.domain.User;
import com.consome.dto.request.LoginRequest;
import com.consome.dto.request.UserValidationRequest;
import com.consome.dto.response.UserResponse;
import com.consome.repository.CurrentPointRepository;
import com.consome.repository.PointHistoryRepository;
import com.consome.repository.User.UserRepository;
import com.consome.repository.User.dsl.UserRepositoryDsl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

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
    public User register(UserValidationRequest user, HttpServletRequest request) {

        //입력 및 중복검증
        validateLoginId(user.getLoginId());
        validateNickname(user.getNickname());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword1(), user.getPassword2()); //2는 입력 비교용
        String clientIp = getClientIp(request);

//        // ✅ 같은 IP에서 24시간 내 가입된 사용자 확인
//        if (userRepositoryDsl.existsRecentSignupByIp(ip)) {
//            throw new IllegalArgumentException("동일 IP에서 24시간 내 가입이 감지되었습니다. 일정 시간이 지난 후 다시 시도하세요.");
//        }

        //비밀번호 암호화
        User createUser = User.createUser(user.getLoginId(),
                user.getNickname(),
                user.getEmail(),
                user.getPassword1(),
                passwordEncoder, clientIp);

        userRepository.save(createUser);

        CurrentPoint currentPoint = CurrentPoint.initCurrentPoint(createUser);
        PointHistory pointHistory = PointHistory.createInitPointHistory(createUser);

        currentPointRepository.save(currentPoint);
        pointHistoryRepository.save(pointHistory);

        return createUser;
    }

    //로그인
    public UserResponse login(LoginRequest request) {
        // 아이디로 먼저 사용자 조회
        Optional<User> userOptional = userRepository.findByLoginId(request.getLoginId());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("아이디를 확인해 주세요.");
        }

        User user = userOptional.get();

        // 입력한 비밀번호와 저장된 해시 값 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        //밴 유무
        if (user.isBanned()) {
            throw new IllegalArgumentException("일시 정지된 사용자 입니다.\n" +
                    "정지 사유 : " + user.getBanReason() + "\n" +
                    "해제 날짜 : " + user.getBanEndDate());
        }

        // JWT 생성
        String accessToken = jwtUtil.generateAccessToken(user.getLoginId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getLoginId());

        // 유저 정보 반환 (토큰 포함)
        return UserResponse.fromEntity(user, accessToken, refreshToken);
    }

    // 아이디 검증 및 중복 검사 (공백 포함 필터링 추가)
    public Map<String, Object> validateLoginId(String loginId) {
        Map<String, Object> response = new HashMap<>();
        String logIdPattern = "^[a-z0-9]{6,12}$";

        // 아이디 형식 검증 (소문자 + 숫자 조합, 12자 이하, 공백 금지)
        if (loginId == null || loginId.trim().isEmpty()) {
            response.put("available", false);
            response.put("message", "아이디를 입력해주세요.");
            return response;
        }
        // 패턴 검증
        if (!Pattern.matches(logIdPattern, loginId)) {
            response.put("available", false);
            response.put("message", "아이디는 공백 없이 영문 소문자와 숫자로 이루어진 6~12자 이내여야 합니다.");
            return response;
        }

        // 아이디 중복 검사
        boolean exists = userRepository.existsByLoginId(loginId);
        response.put("available", !exists);
        response.put("message", exists ? "사용 중인 아이디입니다." : "사용 가능한 아이디입니다.");
        return response;
    }

    // 닉네임 검증 및 중복 검사
    public Map<String, Object> validateNickname(String nickname) {
        Map<String, Object> response = new HashMap<>();

        // 공백 또는 NULL 확인
        if (nickname == null || nickname.isBlank()) {
            response.put("available", false);
            response.put("message", "닉네임을 입력해주세요.");
            return response;
        }

        // 허용된 문자만 포함하는지 확인 (한글, 영문, 숫자만 허용)
        if (!Pattern.matches("^[a-zA-Z0-9가-힣]+$", nickname)) {
            response.put("available", false);
            response.put("message", "닉네임은 한글, 영문, 숫자만 사용할 수 있습니다.");
            return response;
        }

        // 바이트 및 글자 수 제한
        int byteLength = nickname.getBytes(StandardCharsets.UTF_8).length;
        int charLength = nickname.length();
        boolean isKorean = Pattern.matches(".*[가-힣].*", nickname); // 한글 포함 여부

        if ((isKorean && charLength > 10 || (isKorean && charLength < 2)) || (!isKorean && byteLength > 10 || (!isKorean && byteLength < 4))) {
            response.put("available", false);
            response.put("message", "닉네임은 2~10자의 한글, 영문 그리고 숫자의 조합만 사용 가능합니다.");
            return response;
        }
        // 닉네임 중복 체크
        boolean exists = userRepository.existsByNickname(nickname);
        if (exists) {
            response.put("available", false);
            response.put("message", "사용 중인 닉네임입니다.");
        } else {
            response.put("available", true);
            response.put("message", "사용 가능한 닉네임입니다.");
        }

        return response;
    }

    public Map<String, Object> validateEmail(String email) {
        Map<String, Object> response = new HashMap<>();

        // 공백 또는 NULL 확인
        if (email == null || email.isBlank()) {
            response.put("available", false);
            response.put("message", "이메일을 입력해주세요.");
            return response;
        }

        // 이메일 형식 검사 (RFC 5322)
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, email)) {
            response.put("available", false);
            response.put("message", "올바른 이메일 형식을 입력해주세요.");
            return response;
        }

        // 중복 검사
        boolean exists = userRepository.existsByEmail(email);
        if (exists) {
            response.put("available", false);
            response.put("message", "이미 가입된 이메일입니다.");
        } else {
            response.put("available", true);
            response.put("message", "사용 가능한 이메일입니다.");
        }

        return response;
    }

    public Map<String, Object> validatePassword(String password1, String password2) {
        Map<String, Object> response = new HashMap<>();
        // 공백 또는 null 확인
        if (password1 == null || password1.isBlank()) {
            response.put("available", false);
            response.put("message", "비밀번호를 입력해주세요.");
        }
        //비밀번호 형식 검사
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}:;<>,.?~])[A-Za-z\\d!@#$%^&*()_+{}:;<>,.?~]{8,}$";

        boolean isPassword1Valid = Pattern.matches(passwordRegex, password1);
        boolean isPassword2Entered = !password2.isEmpty();
        boolean isPasswordMatch = password1.equals(password2);

        // 기본 응답 초기화
        response.put("available", false);
        response.put("message", "비밀번호는 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.");
        response.put("color", "red");
        response.put("colorClass", "error-text");

        // `password2` 관련 메시지는 입력되었을 때만 추가하도록 초기화하지 않음
        if (isPassword1Valid) {
            response.put("message", "사용 가능한 비밀번호 입니다.");
            response.put("color", "green");
            response.put("colorClass", "success-text");

            // `password2`가 입력되었을 때만 메시지 추가
            if (isPassword2Entered) {
                if (isPasswordMatch) {
                    response.put("available", true);
                    response.put("message2", "비밀번호가 일치 합니다.");
                    response.put("color2", "green");
                    response.put("colorClass2", "success-text");
                } else {
                    response.put("message2", "비밀번호를 똑같이 입력해 주세요.");
                    response.put("color2", "red");
                    response.put("colorClass2", "error-text");
                }
            }
        }
        return response;
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
     */
    public Map<String, Object> findByEmail(String email) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            response.put("available", false);
            response.put("message", "해당 이메일로 가입된 아이디가 없습니다.\n 다시 입력 해 주세요");
        } else {
            response.put("available", true);
            response.put("loginId", findUser.get().getLoginId());
        }
        return response;
    }

    //아이디, 이메일로 비밀번호 찾기
    public Map<String, Object> findByPassword(String loginId, String email) {
        Map<String, Object> response = new HashMap<>();
        long count = userRepositoryDsl.countByLoginIdAndEmail(loginId, email);
        System.out.println("count = " + count);
        if (count == 0) {
            response.put("available", false);
            response.put("message", "아이디 혹은 이메일이 틀렸습니다.");
        } else {
            response.put("available", true);
            response.put("message", "인증이 완료되었습니다. 새 비밀번호를 입력하세요.");
        }
        return response;
    }

    @Transactional
    public Map<String, Object> updatePassword(String loginId, String email, String newPassword, String newPassword2) {
        Map<String, Object> response = validatePassword(newPassword, newPassword2);
        if((boolean) response.get("available")){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodePassword = passwordEncoder.encode(newPassword);
            long l = userRepositoryDsl.updatePassword(loginId, email, encodePassword);
            if(l >0){
                response.put("available", true);
                response.put("message", "비밀번호가 변경되었습니다.");
            }
        } else {
            String message = "";
            if(response.get("message2")==null) {
                message = response.get("message").toString()+ "\n"+"비밀번호를 똑같이 입력해 주세요.";
            } else {
                message = response.get("message").toString() + "\n" + response.get("message2").toString();
            }
            response.put("available", false);
            response.put("message", message);
        }
        return response;
    }



    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        // 프록시 서버를 거치지 않았다면 기본 IP 사용
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }

        // IPv6 로컬 주소 변환
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }
}