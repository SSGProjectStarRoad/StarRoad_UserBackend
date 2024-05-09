package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.dto.UserDTO;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))); // 권한 설정
    }
    // Collections.singletonList 단일 항목을 포함하는 변경 불가능한 리스트 생성. 단일 권한 처리

    // 리프레시 토큰을 이용해 새로운 액세스 토큰을 생성할 때 해당 사용자의 정보를 db에서 조회하려고
    // User 클래스에서 id 필드에 해당하며, @Id 어노테이션으로 지정
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }


    @Override
    public Long save(UserDTO userDTO) {
        User user = User.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .imagePath(userDTO.getImagePath())
                .gender(userDTO.getGender())
                .birth(userDTO.getBirth())
                .phone(userDTO.getPhone())
                .providerType(userDTO.getProviderType())
                .providerId(userDTO.getProviderId())
                .reviewExp(userDTO.getReviewExp())
                .point(userDTO.getPoint())
                .activeStatus(userDTO.getActiveStatus())
                .build();
        return userRepository.save(user).getId();
    }
    public boolean validateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(password, user.getPassword()) && user.isEnabled();
        }
        return false;
    }

}
