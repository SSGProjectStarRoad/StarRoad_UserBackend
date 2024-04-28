package com.ssg.starroad.board.controller;

import com.ssg.starroad.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
}
