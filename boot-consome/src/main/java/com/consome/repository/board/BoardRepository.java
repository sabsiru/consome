package com.consome.repository.board;

import com.consome.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    // 필요한 경우 추가 쿼리 메서드 정의 가능
}