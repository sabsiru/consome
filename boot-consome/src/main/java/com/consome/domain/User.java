package com.consome.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private String phoneNumber; // 전화번호

    @Column(nullable = false)
    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER; // 사용자 권한

    private LocalDateTime createdAt; // 가입 날짜

    private LocalDateTime updatedAt; // 마지막 수정 날짜

    /*정지 관련*/
    @Enumerated(EnumType.STRING)
    private BanType banType = BanType.NO; // 기본값: 정지 아님
    private String banReason; // 정지 사유
    private LocalDateTime banStartDate; // 정지 시작일
    private LocalDateTime banEndDate;   // 정지 해제일

    protected User() {

    }

    // 이용 정지 여부 확인 (NO 제외)
    public boolean isBanned() {
        return banType != BanType.NO && (banEndDate == null || banEndDate.isAfter(LocalDateTime.now()));
    }

    // 🔹 비밀번호 암호화 포함한 정적 생성 메서드
    public static User createUser(String loginId, String nickname, String name, String email, String rawPassword, String phoneNumber, PasswordEncoder passwordEncoder) {
        return new User(
                loginId, nickname, name, email,
                passwordEncoder.encode(rawPassword), // 비밀번호 암호화
                phoneNumber, LocalDateTime.now()
        );
    }

    // 🔹 private 생성자 활용
    private User(String loginId, String nickname, String name, String email, String password, String phoneNumber, LocalDateTime createdAt) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
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