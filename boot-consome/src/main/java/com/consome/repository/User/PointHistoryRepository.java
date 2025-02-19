package com.consome.repository.user;

import com.consome.domain.user.PointHistory;
import com.consome.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory,Long> {
    Optional<PointHistory> findById(Long id);

    Optional<PointHistory> findTopByUserOrderByCreatedAtDesc(User user);

    @Query("select p From PointHistory p where p.user.id = :userId order by p.createdAt desc limit 1")
    Optional<PointHistory> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
