package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.repository.FollowRepository;
import com.ssg.starroad.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
}
