package com.ssg.starroad.board.repository;

import com.ssg.starroad.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
