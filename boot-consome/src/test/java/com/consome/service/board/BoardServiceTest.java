package com.consome.service.board;

import com.consome.controller.BoardController;
import com.consome.domain.board.Board;
import com.consome.dto.board.request.BoardCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardController boardController;

    @Test
    void createBoard() {


    }
}