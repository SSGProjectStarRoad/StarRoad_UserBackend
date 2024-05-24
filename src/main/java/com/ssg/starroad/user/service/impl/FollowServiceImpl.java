package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.dto.FollowDTO;
import com.ssg.starroad.user.entity.Follow;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.FollowRepository;
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

    public long getCountByFromUserId(Long userId) {
        return followRepository.countByFromUserId(userId);
    }

    public long getCountByToUserId(Long userId) {
        return followRepository.countByToUserId(userId);
    }
    public List<FollowDTO> getFollowsByFromUserId(Long fromUserId) {
        List<User> follows = followRepository.findToUsersByFromUserId(fromUserId);
        return follows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FollowDTO> getFollowsByToUserId(Long toUserId) {
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
    public void deleteFollowingUser(Long userid,Long toUserId) {
        followRepository.deleteByFromUserIdAndToUserId(userid, toUserId);
    }



}
