package com.ssg.starroad.user.service;

import java.util.Optional;

public interface UserService {

  Optional<String> findNicknameById(Long id);

}
