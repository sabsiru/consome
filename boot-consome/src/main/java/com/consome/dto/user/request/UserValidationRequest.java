package com.consome.dto.user.request;


import lombok.Getter;

@Getter
public class UserValidationRequest {
    private String loginId;
    private String nickname;
    private String email;
    private String password1;
    private String password2;

    public UserValidationRequest(String loginId, String nickname, String email, String password1) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.email = email;
        this.password1 = password1;
    }
}
