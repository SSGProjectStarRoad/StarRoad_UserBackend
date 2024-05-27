package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.dto.FollowDTO;
import com.ssg.starroad.user.entity.Follow;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.FollowRepository;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.FollowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public long getCountByFromUserId(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        return followRepository.countByFromUserId(userId);
    }

    public long getCountByToUserId(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        return followRepository.countByToUserId(userId);
    }
    public List<FollowDTO> getFollowsByFromUserId(String email) {
        Long fromUserId = userRepository.findByEmail(email).orElseThrow().getId();
        List<User> follows = followRepository.findToUsersByFromUserId(fromUserId);
        return follows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FollowDTO> getFollowsByToUserId(String email) {
        Long toUserId = userRepository.findByEmail(email).orElseThrow().getId();
        List<User> follows = followRepository.findFromUsersByToUserId(toUserId);
        return follows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private FollowDTO convertToDTO(User user) {
        return FollowDTO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImgUrl(user.getImagePath())
                .build();
    }

    @Transactional
    @Override
    public void deleteFollowingUser(String email,Long toUserId) {
        Long userid = userRepository.findByEmail(email).orElseThrow().getId();
        followRepository.deleteByFromUserIdAndToUserId(userid, toUserId);
    }



}
