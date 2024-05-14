package com.ssg.starroad.user.service;

import com.ssg.starroad.user.dto.FollowDTO;
import com.ssg.starroad.user.entity.Follow;

import java.util.List;

public interface FollowService {
    public long getCountByFromUserId(Long userId);
    public long getCountByToUserId(Long userId);
    public List<FollowDTO> getFollowsByToUserId(Long toUserId);
    public List<FollowDTO> getFollowsByFromUserId(Long fromUserId);
    public void deleteFollowingUser(Long userid,Long toUserId);
}
