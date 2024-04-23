package com.ssg.starroad.user.repository;

import com.ssg.starroad.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
