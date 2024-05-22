package com.ssg.starroad.user.service;

import java.util.Optional;
import com.ssg.starroad.user.dto.MypageDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Optional<String> findNicknameById(Long id);
    public MypageDTO getMypage(Long id);
    public void saveProfileimg(Long id, String path);
    public String getProfileimg(Long id);
    public void deleteProfileimg(Long id);
}
