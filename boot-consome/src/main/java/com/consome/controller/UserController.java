package com.consome.controller;

import com.consome.domain.User;
import com.consome.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            User userId = userService.register(user);
            return ResponseEntity.ok("회원가입 성공 (ID: " + userId.getLoginId() + ")");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

    @PostMapping("/sign")
    public String sign(@RequestBody Map<String,String> request) {
        String username = request.get("loginId");
        System.out.println("request = " + request);
        System.out.println("username = " + username);

        return "회원가입 성공";
    }
}