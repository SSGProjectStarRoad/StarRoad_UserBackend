package com.ssg.starroad.user.repository;

import com.ssg.starroad.user.entity.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository extends JpaRepository<AuthCode, String> {
}
