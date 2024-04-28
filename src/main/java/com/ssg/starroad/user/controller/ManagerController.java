package com.ssg.starroad.user.controller;

import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/manager")
@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final UserService userService;
}
