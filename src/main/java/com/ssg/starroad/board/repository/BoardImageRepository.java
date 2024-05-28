package com.ssg.starroad.board.repository;

import com.ssg.starroad.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    public BoardImage findByBoardId(Long boardId);
}
