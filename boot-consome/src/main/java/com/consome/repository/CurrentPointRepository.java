package com.consome.repository;

import com.consome.domain.CurrentPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentPointRepository extends JpaRepository<CurrentPoint, Long> {
}
