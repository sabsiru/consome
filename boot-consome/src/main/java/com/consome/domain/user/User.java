package com.consome.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "unique_email", columnNames = "email"),
        @UniqueConstraint(name = "unique_social_id", columnNames = "socialId")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = true) // 소셜 로그인은 loginId 없이 가능
    private String loginId;

    @Column(length = 50, unique = true, nullable = false)
    private String nickname;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(length = 255, nullable = true) // 소셜 로그인 사용자는 비밀번호가 없음
    private String password;

    @Column(length = 100, unique = true, nullable = true)
    private String socialId; // 소셜 로그인 ID (네이버, 카카오, 구글 등)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role=  Role.ROLE_USER;

    @Column(nullable = false)
    private int point = 100;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BanType banType = BanType.NO;

    @Column(length = 255)
    private String banReason;

    private LocalDateTime banStartDate;

    private LocalDateTime banEndDate;

    @Column(length = 45)
    private String lastSignupIp; // 최근 가입 IP 저장

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 이용 정지 여부 확인 (NO 제외)
    public boolean isBanned() {
        return banType != BanType.NO && (banEndDate == null || banEndDate.isAfter(LocalDateTime.now()));
    }

    // 🔹 비밀번호 암호화 포함한 정적 생성 메서드
    public static User createUser(String loginId, String nickname, String email, String rawPassword, PasswordEncoder passwordEncoder,String ip) {
        return new User(
                loginId, nickname, email,
                passwordEncoder.encode(rawPassword), // 비밀번호 암호화
                LocalDateTime.now(),ip
        );
    }

    public User(String loginId,String nickname,String email,String password){
        this.loginId = loginId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    // 🔹 private 생성자 활용
    private User(String loginId, String nickname, String email, String password, LocalDateTime createdAt, String ip) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.lastSignupIp = ip;
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
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 마지막 가입한 IP 주소 업데이트
     */
    public void updateSignupIp(String ip) {
        this.lastSignupIp = ip;
    }

    /**
     * 소셜 로그인 사용자 생성
     */
    public static User createSocialUser(String nickname, String email, String socialId) {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .socialId(socialId)
                .role(Role.ROLE_USER)
                .point(100) // 기본 포인트
                .banType(BanType.NO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}