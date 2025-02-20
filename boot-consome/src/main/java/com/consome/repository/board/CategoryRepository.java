package com.consome.repository.board;

import com.consome.domain.board.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // 추가적인 쿼리 메서드가 필요하면 여기서 정의
}