package com.consome.repository.User.dsl;

import com.consome.domain.QUser;
import com.consome.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryDsl {
    private final JPAQueryFactory queryFactory;

    /**
     * 로그인 시, 아이디와 비밀번호(암호화된)를 확인하여 사용자 조회
     * @param loginId 로그인 아이디
     * @param password 암호화된 비밀번호
     * @return Optional<User>
     */
    public Optional<User> findByLoginIdAndPassword(String loginId, String password) {
        QUser user = QUser.user;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(user)
                        .where(
                                user.loginId.eq(loginId),
                                user.password.eq(password)  // 비밀번호는 암호화된 값과 비교
                        )
                        .fetchOne()
        );
    }
}
