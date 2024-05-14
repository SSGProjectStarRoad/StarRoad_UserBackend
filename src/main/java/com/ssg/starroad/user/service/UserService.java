package com.ssg.starroad.user.service;

import com.ssg.starroad.user.dto.MypageDTO;

public interface UserService {
    public MypageDTO getMypage(Long id);
}
