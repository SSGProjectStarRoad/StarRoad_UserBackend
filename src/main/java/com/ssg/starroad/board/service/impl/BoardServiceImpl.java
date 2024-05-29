package com.ssg.starroad.board.service.impl;

import com.ssg.starroad.board.DTO.BoardDTO;
import com.ssg.starroad.board.enums.BoardCategory;
import com.ssg.starroad.board.repository.BoardRepository;
import com.ssg.starroad.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<BoardDTO> getEventBoards() {
        return boardRepository.findByCategory(BoardCategory.EVENT).stream().map(board ->
                modelMapper.map(board,BoardDTO.class)).toList();
    }
}
