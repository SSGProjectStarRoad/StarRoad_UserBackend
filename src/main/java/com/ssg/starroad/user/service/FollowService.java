package com.ssg.starroad.user.service;

import com.ssg.starroad.user.dto.FollowDTO;
import com.ssg.starroad.user.entity.Follow;

import java.util.List;

public interface FollowService {
    public long getCountByFromUserId(String email);
    public long getCountByToUserId(String email);
    public List<FollowDTO> getFollowsByToUserId(String email);
    public List<FollowDTO> getFollowsByFromUserId(String email);
    public void deleteFollowingUser(String email,Long toUserId);
}
