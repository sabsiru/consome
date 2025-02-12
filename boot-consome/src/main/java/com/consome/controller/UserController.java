package com.consome.controller;

import com.consome.auth.JwtUtil;
import com.consome.domain.User;
import com.consome.dto.request.LoginRequest;
import com.consome.dto.request.UserValidationRequest;
import com.consome.dto.response.UserResponse;
import com.consome.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserValidationRequest user, HttpServletRequest request) {
        try {
            User userId = userService.register(user, request);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        UserResponse userResponse = userService.login(request);

        // ✅ JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(userResponse.getLoginId());
        String refreshToken = jwtUtil.generateRefreshToken(userResponse.getLoginId());

        // ✅ Authorization 헤더 설정 (Bearer 토큰)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Refresh-Token", refreshToken);  // Refresh 토큰은 프론트에서 필요하면 사용

        return ResponseEntity.ok()
                .headers(headers)
                .body(userResponse);
    }

    // 아이디 중복 검사 및 입력 검증
    @PostMapping("/validLoginId")
    public ResponseEntity<?> validateLoginId(@RequestBody UserValidationRequest request) {
        return ResponseEntity.ok(userService.validateLoginId(request.getLoginId()));
    }

    // 닉네임 중복 검사 및 입력 검증
    @PostMapping("/validNickname")
    public ResponseEntity<Map<String, Object>> checkNickname(@RequestBody UserValidationRequest request) {
        return ResponseEntity.ok(userService.validateNickname(request.getNickname()));
    }

    // 이메일 중복 검사 및 입력 검증
    @PostMapping("/validEmail")
    public ResponseEntity<Map<String, Object>> validateEmail(@RequestBody UserValidationRequest request) {
        return ResponseEntity.ok(userService.validateEmail(request.getEmail()));
    }

    // 비밀번호 입력 검증
    @PostMapping("/validPassword")
    public ResponseEntity<Map<String, Object>> validatePassword(@RequestBody UserValidationRequest request) {
        return ResponseEntity.ok(userService.validatePassword(request.getPassword1(), request.getPassword2()));
    }

    //이메일로 아이디 찾기
    @PostMapping("/findLoginId")
    public ResponseEntity<Map<String, Object>> findLoginIdByEmail(@RequestBody UserValidationRequest request) {
        return ResponseEntity.ok(userService.findByEmail(request.getEmail()));
    }

    //아이디, 이메일로 비밀번호
    @PostMapping("/findPassword")
    public ResponseEntity<Map<String, Object>> findPasswordByEmail(@RequestBody UserValidationRequest request) {
        return ResponseEntity.ok(userService.findByPassword(request.getLoginId(), request.getEmail()));
    }

    //비밀번호 업데이트
    @PostMapping("/updatePassword")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestBody UserValidationRequest request) {
        return ResponseEntity.ok(userService.updatePassword(request.getLoginId(), request.getEmail(), request.getPassword1(), request.getPassword2()));
    }

}