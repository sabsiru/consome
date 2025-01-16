package com.consome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.consome.domain.User;
import java.util.Optional;

/**
 * UserRepository
 *
 * 사용자 정보를 데이터베이스와 상호작용하기 위한 JPA 리포지토리 인터페이스.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 로그인 ID로 사용자를 조회합니다.
     *
     * @param loginId 로그인 ID
     * @return 로그인 ID에 해당하는 사용자
     */
    Optional<User> findByLoginId(String loginId);

    /**
     * 이메일로 사용자를 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 이메일에 해당하는 사용자
     */
    Optional<User> findByEmail(String email);

    /**
     * 닉네임으로 사용자를 조회합니다.
     *
     * @param nickname 사용자 닉네임
     * @return 닉네임에 해당하는 사용자
     */
    Optional<User> findByNickname(String nickname);

    /**
     * 전화번호로 사용자를 조회합니다.
     *
     * @param phoneNumber 사용자 닉네임
     * @return 전화번호에 해당하는 사용자
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * 전화번호 혹은 이메일로 사용자를 조회
     *
     * @param phoneNumber
     * @return 전화
     * */
}