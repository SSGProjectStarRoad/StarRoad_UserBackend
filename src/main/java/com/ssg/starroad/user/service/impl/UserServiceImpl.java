package com.ssg.starroad.user.service.impl;


import com.ssg.starroad.user.dto.MypageDTO;
import com.ssg.starroad.user.dto.RankUserDTO;
import com.ssg.starroad.user.dto.UserDTO;
import com.ssg.starroad.user.entity.Follow;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.ProviderType;
import com.ssg.starroad.user.repository.FollowRepository;
import com.ssg.starroad.user.repository.UserRepository;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UrlBasedCorsConfigurationSource corsConfigurationSource;
    private final FollowRepository followRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email));

        return user; // User 엔티티 반환
    }

    // 리프레시 토큰을 이용해 새로운 액세스 토큰을 생성할 때 해당 사용자의 정보를 db에서 조회하기 위해
    // User 클래스에서 id 필드에 해당하며, @Id 어노테이션으로 지정
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    // OAuth2에서 제공하는 이메일은 유일 값이므로 이 메서드를 사용해 유저 찾기 가능
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
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
                .providerType(ProviderType.LOCAL)
                .providerId(userDTO.getProviderId())
                .reviewExp(userDTO.getReviewExp())
                .point(userDTO.getPoint())
                .activeStatus(userDTO.getActiveStatus())
                .build();
        return userRepository.save(user).getId();
    }

    public boolean validateUser(String email, String password) {

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            System.out.println("No user found with email: " + email);
            return false;
        }

        User user = userOpt.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean passwordMatches = encoder.matches(password, user.getPassword());

        if (!passwordMatches) {
            System.out.println("Password mismatch for user: " + email);
        }

        return passwordMatches;
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        User user = User.builder()
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .gender(userDTO.getGender())
                .birth(userDTO.getBirth())
                .phone(userDTO.getPhone())
                .providerType(ProviderType.LOCAL)
                .providerId(null)
                .reviewExp(0)
                .point(0)
                .activeStatus(ActiveStatus.ACTIVE)
                .build();

        userRepository.save(user);
    }

    @Override
    public Optional<String> findNicknameById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(User::getNickname);
    }

    public MypageDTO getMypage(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return MypageDTO.builder()
                .nickname(user.getNickname())
                .profileImg(user.getImagePath())
                .reviewExp(user.getReviewExp())
                .name(user.getUserName())
                .point(user.getPoint())
                .build();
    }

    @Override
    public void saveProfileimg(String email, String path) {

        User user = userRepository.findByEmail(email).orElseThrow();
        user.setProfileimgPath(path);
        userRepository.save(user);
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        System.out.println("Searching for user with email: " + email); // 로그 추가
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            System.out.println("User found: " + user); // 로그 추가
            user.updatePassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        System.out.println("User not found with email: " + email); // 로그 추가
        return false;
    }

    @Transactional
    @Override
    public void updateUserProfile(User user, UserDTO userDTO) {
        String encodedPassword = user.getPassword(); // 기존 비밀번호로 초기화

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            // 비밀번호가 비어있지 않을 경우에만 비밀번호 업데이트
            encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        }

        user.updateProfile(userDTO.getNickname(), encodedPassword);

        userRepository.save(user);
        System.out.println("Updated User: " + user);
        System.out.println("Updated Nickname: " + user.getNickname());
        System.out.println("Updated Password: " + user.getPassword());
    }

    @Override
    public void inactiveUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.changeActiveStatus(ActiveStatus.INACTIVE);
        userRepository.save(user);
    }

    public String getProfileimg(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return user.getImagePath();
    }

    @Override
    public void deleteProfileimg(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setProfileimgPath("");
        userRepository.save(user);
    }

    @Override
    public List<RankUserDTO> getRankUser(String email) {
        List<User> users = userRepository.findAll();
        Long currentUserId = findByEmail(email).get().getId();

        // id 값과 동일한 값을 제외하고 나머지 사용자들 선택
        List<User> filteredUsers = users.stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .collect(Collectors.toList());

        // reviewExp 필드를 기준으로 내림차순 정렬
        List<User> topUsers = filteredUsers.stream()
                .sorted(Comparator.comparingInt(User::getReviewExp).reversed())
                .collect(Collectors.toList());

        topUsers.forEach(System.out::println);

        // User 객체를 RankUserDTO 객체로 변환
        List<RankUserDTO> rankUserDTOS = topUsers.stream()
                .map(user -> {
                    boolean isFollowed = followRepository.existsByFromUserIdAndToUserId(currentUserId, user.getId());
                    return RankUserDTO.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .nickname(user.getNickname())
                            .email(user.getEmail())
                            .imagePath(user.getImagePath())
                            .reviewExp(user.getReviewExp())
                            .point(user.getPoint())
                            .activeStatus(user.getActiveStatus())
                            .isFollowed(isFollowed)
                            .build();
                })
                .collect(Collectors.toList());

        return rankUserDTOS;
    }


    @Override
    public List<RankUserDTO> getAllUser(String email) {
        List<User> users = userRepository.findAll();

        Long id = findByEmail(email).get().getId();

        // id 값과 동일한 값을 제외하고 나머지 사용자들 선택
        List<User> filteredUsers = users.stream()
                .filter(user -> !user.getId().equals(id))
                .collect(Collectors.toList());

        // reviewExp 필드를 기준으로 상위 3명의 사용자만 선택
        List<User> topUsers = users.stream()
                .sorted(Comparator.comparingInt(User::getReviewExp).reversed())
                .collect(Collectors.toList());

        // 상위 3명의 사용자 목록을 출력 또는 다른 작업 수행
        topUsers.forEach(System.out::println);

        // User 객체를 RankUserDTO 객체로 변환
        List<RankUserDTO> rankUserDTOS = topUsers.stream()
                .map(user -> RankUserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .imagePath(user.getImagePath())
                        .reviewExp(user.getReviewExp())
                        .point(user.getPoint())
                        .activeStatus(user.getActiveStatus())
                        .build())
                .collect(Collectors.toList());

        return rankUserDTOS;
    }


    @Transactional
    public String addFollowUser(String userName, String email) {
        System.out.println("userName, email : " + userName + ", " + email);
        Optional<User> fromUserOpt = userRepository.findByEmail(email);
        Optional<User> toUserOpt = userRepository.findBynickname(userName);
        System.out.println("fromUserOpt, toUserOpt : " + fromUserOpt + ", " + toUserOpt);
        if (!fromUserOpt.isPresent() || !toUserOpt.isPresent()) {
            return "유효하지 않은 사용자 정보입니다.";
        }

        User fromUser = fromUserOpt.get();
        User toUser = toUserOpt.get();

        System.out.println("fromUser, toUser : " + fromUser + ", " + toUser);
        System.out.println("toUser tostring : " + toUser.toString());

        Optional<Follow> existingFollow = followRepository.findByFromUserAndToUser(fromUser, toUser);

        // 기존 팔로우 관계가 존재하면 삭제, 그렇지 않으면 새로운 팔로우 관계 추가
        if (existingFollow.isPresent()) {
            System.out.println("해제 실행");
            followRepository.delete(existingFollow.get());
            return "팔로우 관계가 해제되었습니다.";
        } else {
            Follow newFollow = Follow.builder()
                    .fromUser(fromUser)
                    .toUser(toUser)
                    .build();
            System.out.println("저장 실행");
            followRepository.save(newFollow);
            return "새로운 팔로우 관계가 생성되었습니다.";
        }
    }
}
