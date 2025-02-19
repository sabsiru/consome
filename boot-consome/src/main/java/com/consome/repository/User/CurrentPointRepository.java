package com.consome.repository.user;

import com.consome.domain.user.CurrentPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentPointRepository extends JpaRepository<CurrentPoint, Long> {
}
