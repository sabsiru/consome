package com.consome.dto.board.request;

import lombok.Data;

@Data
public class BoardCreateRequest {
    private String name;
    private String description;
    private Integer parentId; // 부모 게시판 ID (최상위 게시판이면 null)
    private Integer sortOrder; // 게시판 정렬 순서 (선택사항)
}