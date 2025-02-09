package com.consome.dto.response;

import com.consome.domain.BanType;
import com.consome.domain.Role;
import com.consome.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {
    private final Long id;
    private final String loginId;
    private final String nickname;
    private final String email;
    private final Role role;
    private final int point;
    private final BanType banType;
    private final LocalDateTime banEndDate;
    private final boolean isBanned;
    private final String accessToken;  // ✅ JWT 액세스 토큰 추가
    private final String refreshToken;

    @Builder
    private UserResponse(Long id, String loginId, String nickname, String email, Role role, int point, BanType banType, LocalDateTime banEndDate, boolean isBanned, String accessToken,String refreshToken) {
        this.id = id;
        this.loginId = loginId;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.point = point;
        this.banType = banType;
        this.banEndDate = banEndDate;
        this.isBanned = isBanned;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // ✅ User 엔티티 → UserResponse 변환 (토큰 포함)
    public static UserResponse fromEntity(User user, String accessToken,String refreshToken) {
        boolean isBanned = user.getBanType() != BanType.NO &&
                (user.getBanEndDate() == null || user.getBanEndDate().isAfter(LocalDateTime.now()));

        return UserResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole())
                .point(user.getPoint()) // ✅ 포인트 추가
                .banType(user.getBanType()) // ✅ 밴 상태 추가
                .banEndDate(user.getBanEndDate())
                .isBanned(isBanned)
                .accessToken(accessToken) // ✅ 토큰 포함
                .refreshToken(accessToken)
                .build();
    }
}