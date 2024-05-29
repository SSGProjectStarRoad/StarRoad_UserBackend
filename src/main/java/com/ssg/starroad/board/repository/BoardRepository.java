package com.ssg.starroad.board.repository;

import com.ssg.starroad.board.entity.Board;
import com.ssg.starroad.board.enums.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    public List<Board> findByCategory(BoardCategory category);
}
