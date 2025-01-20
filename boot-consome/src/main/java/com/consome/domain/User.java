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
    private String phoneNumber; // 전화번호

    @Column(nullable = false)
    private String password; // 비밀번호 (테스트용)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 사용자 권한

    private int currentPoint; // 현재 포인트

    private LocalDateTime createdAt; // 가입 날짜

    private LocalDateTime updatedAt; // 마지막 수정 날짜

    public User() {

    }

    // 사용자 권한 Enum
    public enum Role {
        USER, ADMIN, SUPERADMIN
    }

    //유저 생성
    public static User createUser(String loginId, String nickname, String name, String email, String password, String phoneNumber) {
        User user = new User();
        user.loginId = loginId;
        user.nickname = nickname;
        user.name = name;
        user.email = email;
        user.password = password; // 비밀번호는 암호화된 상태로 전달되어야 함
        user.role = Role.USER;
        user.createdAt = LocalDateTime.now();
        user.phoneNumber = formatPhoneNumber(phoneNumber);
        return user;
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

    //포인트 업데이트
    public void setCurrentPoint(int currentPoint) {
        this.currentPoint = currentPoint;
    }

    //전화번호 포맷팅 메서드
    private static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 11 || !phoneNumber.matches("\\d{11}")) {
            System.out.println("phoneNumber = " + phoneNumber);
            throw new IllegalArgumentException("전화번호는 숫자 11자리여야 합니다.");
        }
        return phoneNumber.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
    }
}