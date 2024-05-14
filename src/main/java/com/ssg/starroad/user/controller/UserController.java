package com.ssg.starroad.user.controller;

import com.ssg.starroad.user.dto.MypageDTO;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/mypage/{userId}")
    public ResponseEntity<MypageDTO> getMypage(@PathVariable Long userId) {
        MypageDTO mypageDTO=userService.getMypage(userId);
        if (mypageDTO==null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mypageDTO);
    }
}
