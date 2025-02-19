package com.consome.controller;

import com.consome.domain.board.Board;
import com.consome.dto.board.request.BoardCreateRequest;
import com.consome.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 모든 게시판의 이름을 조회하여 반환합니다.
     * GET /api/boards/names
     */
    @GetMapping("/names")
    public List<String> getBoardNames() {
        return boardService.getAllBoardNames();
    }

    /**
     * 관리자만 게시판 생성할 수 있도록 제한합니다.
     * POST /api/boards/create
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createBoard(@RequestBody BoardCreateRequest request) {
        Board board = boardService.createBoard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(board);
    }
}