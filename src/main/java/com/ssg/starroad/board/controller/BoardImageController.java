package com.ssg.starroad.board.controller;

import com.ssg.starroad.board.DTO.BoardContentDTO;
import com.ssg.starroad.board.DTO.BoardDTO;
import com.ssg.starroad.board.service.BoardImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board-image")
public class BoardImageController {
    private final BoardImageService boardImageService;

    @GetMapping("/content/{boardId}")
    public ResponseEntity<BoardContentDTO> getBoardContent(@PathVariable Long boardId) {
        BoardContentDTO boardContent = boardImageService.getEventContent(boardId);
        if (boardContent==null) {
            return ResponseEntity.noContent().build();  // 내용이 없을 경우 No Content (204) 반환
        }
        return ResponseEntity.ok(boardContent);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환
    }
}
