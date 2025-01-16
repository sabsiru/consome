package com.consome.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "password") // 비밀번호는 toString에서 제외
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 고유 ID

    @Column(nullable = false, unique = true, length = 50)
    private String loginId; // 로그인용 ID

    @Column(nullable = false, unique = true, length = 50)
    private String nickname; // 사용자 닉네임

    @Column(nullable = false, length = 50)
    private String name; // 사용자 이름

    @Column(nullable = false, unique = true, length = 100)
    private String email; // 사용자 이메일

    @Column(length = 15)
    @Setter
    private String phoneNumber; // 전화번호

    @Column(nullable = false)
    private String password; // 비밀번호 (테스트용)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Role role; // 사용자 권한

    @Setter
    private LocalDateTime createdAt; // 가입 날짜

    private LocalDateTime updatedAt; // 마지막 수정 날짜

    public User() {

    }

    // 사용자 권한 Enum
    public enum Role {
        USER, ADMIN, SUPERADMIN
    }

    public User(String loginId, String nickname, String name, String email, String password, String phoneNumber) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    // 생성자
    public User(String loginId, String nickname, String name, String email, String phoneNumber, String password, Role role) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password; // 테스트용 비밀번호
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 비밀번호 변경 메서드
    public void changePassword(String newPassword) {
        this.password = newPassword; // 새로운 비밀번호 설정
        this.updatedAt = LocalDateTime.now();
    }

    // 사용자 정보 수정 메서드
    public void updateProfile(String nickname, String email, String phoneNumber) {
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.updatedAt = LocalDateTime.now();
    }
}