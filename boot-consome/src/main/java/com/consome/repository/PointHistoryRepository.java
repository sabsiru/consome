package com.consome.repository;

import com.consome.domain.PointHistory;
import com.consome.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory,Long> {
    Optional<PointHistory> findById(Long id);

    Optional<PointHistory> findTopByUserOrderByCreatedAtDesc(User user);

}
