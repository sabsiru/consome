package com.consome.dto.request;


import lombok.Getter;

@Getter
public class UserValidationRequest {
    private String loginId;
    private String nickname;
    private String email;
    private String password;
}
