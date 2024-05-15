package com.ssg.starroad.user.service;

import java.util.Optional;
import com.ssg.starroad.user.dto.MypageDTO;

public interface UserService {
    Optional<String> findNicknameById(Long id);
    public MypageDTO getMypage(Long id);
}
