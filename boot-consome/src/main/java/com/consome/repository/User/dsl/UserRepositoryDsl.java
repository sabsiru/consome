package com.consome.repository.User.dsl;

import com.consome.domain.QUser;
import com.consome.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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


    /**
     * 특정 IP에서 최근 24시간 이내 가입한 사용자가 있는지 확인
     */
    public boolean existsRecentSignupByIp(String ip) {
        QUser user = QUser.user;

        // 24시간 이내 가입한 사용자 검색
        Long count = queryFactory
                .select(user.count())
                .from(user)
                .where(
                        user.lastSignupIp.eq(ip),
                        user.createdAt.after(LocalDateTime.now().minusHours(24)) // 24시간 내 가입 여부
                )
                .fetchOne();

        return count != null && count > 0;
    }

    /**
    * loginId와 email로 count 조회
    * */
    public long countByLoginIdAndEmail(String loginId, String email) {
        QUser user = QUser.user;
        return queryFactory
                .select(user.count())
                .from(user)
                .where(user.loginId.eq(loginId)
                        .and(user.email.eq(email))
                )
                .fetchOne();
    }

    /**
    * loginId, email, password update
    * */
    public long updatePassword(String loginId, String email, String newPassword) {
        QUser user = QUser.user;
        return queryFactory
                .update(user)
                .set(user.password, newPassword) // ✅ 비밀번호 업데이트
                .where(user.loginId.eq(loginId) // ✅ 첫 번째 조건
                        .and(user.email.eq(email))) // ✅ 두 번째 조건
                .execute();
    }

}
