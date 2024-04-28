package com.ssg.starroad.user.service.impl;

import com.ssg.starroad.user.repository.ManagerRepository;
import com.ssg.starroad.user.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
}
