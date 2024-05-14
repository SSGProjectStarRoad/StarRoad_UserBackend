package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.dto.MypageDTO;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;


    public MypageDTO getMypage(Long id) {
        return userRepository.findById(id).map(info -> modelMapper.map(info, MypageDTO.class)).orElse(null);
    }

}
