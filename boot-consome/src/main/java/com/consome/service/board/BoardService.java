package com.consome.service.board;

import com.consome.domain.board.Board;
import com.consome.dto.board.request.BoardCreateRequest;
import com.consome.repository.board.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 모든 게시판의 이름을 조회하여 반환합니다.
     *
     * @return 게시판 이름 리스트
     */
    public List<String> getAllBoardNames() {
        return boardRepository.findAll()
                .stream()
                .map(Board::getName)
                .collect(Collectors.toList());
    }
    /**
     * 새로운 게시판을 생성합니다.
     *
     * @return 생성된 Board 엔티티
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public Board createBoard(BoardCreateRequest request) {
        Board parent = null;
        if (request.getParentId() != null) {
            parent = boardRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 부모 게시판 ID입니다."));
        }
        Board board = Board.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent(parent)
                .sortOrder(request.getSortOrder()) // 정렬 순서 설정
                .build();
        return boardRepository.save(board);
    }
}