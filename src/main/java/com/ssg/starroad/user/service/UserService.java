package com.ssg.starroad.user.service;


import com.ssg.starroad.user.dto.UserDTO;
import com.ssg.starroad.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.ssg.starroad.user.dto.MypageDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User findById(Long userId);

    Optional<User> findByEmail(String email);

    Long save(UserDTO userDTO);

    boolean validateUser(String email, String password);

    boolean isEmailDuplicate(String email); // 이메일 중복 검사

    boolean isNicknameDuplicate(String nickname); // 닉네임 중복 검사

    void registerUser(UserDTO userDTO);

    boolean updatePassword(String email, String newPassword);

    void updateUserProfile(User user, UserDTO userDTO);

    void inactiveUser(String email);

    Optional<String> findNicknameById(Long id);
    public MypageDTO getMypage(String email);
    public void saveProfileimg(String email, String path);
    public String getProfileimg(String email);
    public void deleteProfileimg(String email);

}
