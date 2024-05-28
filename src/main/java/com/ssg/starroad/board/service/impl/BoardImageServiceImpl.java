package com.ssg.starroad.board.service.impl;

import com.ssg.starroad.board.DTO.BoardContentDTO;
import com.ssg.starroad.board.repository.BoardImageRepository;
import com.ssg.starroad.board.service.BoardImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardImageServiceImpl implements BoardImageService {
    private final BoardImageRepository boardImageRepository;
    private final ModelMapper modelMapper;
    @Override
    public BoardContentDTO getEventContent(Long boardid) {
        return modelMapper.map(boardImageRepository.findByBoardId(boardid), BoardContentDTO.class);
    }
}
