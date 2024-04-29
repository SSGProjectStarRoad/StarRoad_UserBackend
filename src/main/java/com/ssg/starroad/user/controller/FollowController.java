package com.ssg.starroad.user.controller;

import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/follow")
@RestController
@RequiredArgsConstructor
public class FollowController {

    private final UserService userService;
}
