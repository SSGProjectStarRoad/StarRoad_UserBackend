package com.ssg.starroad.user.service.impl;


import com.ssg.starroad.user.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImplTest {
    private static final Logger log = LogManager.getLogger(UserServiceImplTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void getUserNickname() {
        String name = String.valueOf(userService.findNicknameById(2L));
        log.info(name);

    }
}