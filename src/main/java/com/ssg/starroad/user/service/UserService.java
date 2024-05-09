package com.ssg.starroad.user.service;

import com.ssg.starroad.user.dto.UserDTO;
import com.ssg.starroad.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findById(Long userId);

    User findByEmail(String email);

    Long save(UserDTO userDTO);

    boolean validateUser(String email, String password);
}
