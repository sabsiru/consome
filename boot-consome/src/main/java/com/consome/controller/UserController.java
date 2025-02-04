package com.consome.controller;

import com.consome.domain.User;
import com.consome.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            Long userId = userService.register(user);
            return ResponseEntity.ok("회원가입 성공 (ID: " + userId + ")");
        } catch (IllegalArgumentException e) {
            // ❌ 중복 오류 발생 시 예외 메시지를 JSON으로 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }
}