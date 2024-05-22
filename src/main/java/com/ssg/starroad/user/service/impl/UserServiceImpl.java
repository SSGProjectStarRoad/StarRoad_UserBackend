package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.dto.MypageDTO;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Optional<String> findNicknameById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(User::getNickname);
    }

    public MypageDTO getMypage(Long id) {
        return userRepository.findById(id).map(info -> modelMapper.map(info, MypageDTO.class)).orElse(null);
    }

    @Override
    public void saveProfileimg(Long id, String path) {
        User user=userRepository.findById(id).orElseThrow();
        user.setProfileimgPath(path);
        userRepository.save(user);
    }

    @Override
    public String getProfileimg(Long id) {
        User user =userRepository.findById(id).orElseThrow();
        return user.getImagePath();
    }

    @Override
    public void deleteProfileimg(Long id) {
        User user =userRepository.findById(id).orElseThrow();
        user.setProfileimgPath("");
        userRepository.save(user);
    }
}
