package com.consome.service.board;

import com.consome.domain.board.Category;
import com.consome.domain.board.Board;
import com.consome.repository.board.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 특정 게시판에 속하는 카테고리를 생성합니다.
     *
     * @param board       해당 카테고리가 속할 게시판 엔티티
     * @param name        카테고리 이름 (예: 자유, 팁과정보, 유출, 자랑 등)
     * @param description 카테고리 설명
     * @param sortOrder   카테고리 정렬 순서 (선택사항)
     * @return 생성된 Category 엔티티
     */
    @Transactional
    public Category createCategory(Board board, String name, String description, Integer sortOrder) {
        Category category = Category.builder()
                .board(board)
                .name(name)
                .description(description)
                .sortOrder(sortOrder)
                .build();
        return categoryRepository.save(category);
    }
}