package com.ssg.starroad.user.controller;

import com.ssg.starroad.common.service.S3Uploader;
import com.ssg.starroad.user.dto.MypageDTO;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final S3Uploader s3Uploader;

    @GetMapping("/mypage/{userId}")
    public ResponseEntity<MypageDTO> getMypage(@PathVariable Long userId) {
        MypageDTO mypageDTO=userService.getMypage(userId);
        if (mypageDTO==null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mypageDTO);
    }

    @PostMapping("/profile/upload/img/{userId}")
    public ResponseEntity<?> uploadImg(@PathVariable Long userId, @RequestParam("file") MultipartFile uploadImg) {
        String path = s3Uploader.upload(uploadImg,"ssg/user/profile");
        userService.saveProfileimg(userId,path);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/get/img/{userId}")
    public ResponseEntity<?> getImg(@PathVariable Long userId)
    {
        String path =userService.getProfileimg(userId);
        if (path==null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(path.trim());
    }

    @DeleteMapping("/profile/delete/img/{userId}")
    public ResponseEntity<?> deleteImg(@PathVariable Long userId){
        userService.deleteProfileimg(userId);
        return ResponseEntity.ok().build();
    }
}
