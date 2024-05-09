package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<String> findNicknameById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);



        return userOptional.map(User::getNickname);
    }
}
