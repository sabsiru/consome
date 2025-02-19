package com.consome.dto.user.request;

import lombok.Data;

/**
 * UserRegistrationRequest
 *
 * 회원가입 요청 데이터를 캡슐화하는 DTO.
 */
@Data
public class UserRegistrationRequest {

    /** 로그인 ID */
    private String loginId;

    /** 닉네임 */
    private String nickname;

    /** 사용자 이름 */
    private String name;

    /** 이메일 */
    private String email;

    /** 전화번호 */
    private String phoneNumber;

    /** 비밀번호 */
    private String password;
}