package com.ssg.starroad.board.controller;

import com.ssg.starroad.board.DTO.BoardDTO;
import com.ssg.starroad.board.service.BoardService;
import com.ssg.starroad.coupon.DTO.CouponDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;


    @GetMapping("/list")
    public ResponseEntity<List<BoardDTO>> getBoardList() {
        List<BoardDTO> boardDTOS = boardService.getEventBoards();
        if (boardDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();  // 내용이 없을 경우 No Content (204) 반환
        }
        return ResponseEntity.ok(boardDTOS);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환
    }
}
