package com.ssg.starroad.user.repository;

import com.ssg.starroad.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByid(Long id);

    Optional<User> findBynickname(String nickName);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.reviewExp = :reviewExp WHERE u.id = :id")
    int updateReviewExpById(@Param("id") Long id, @Param("reviewExp") int reviewExp);

    // point 필드를 업데이트하는 메소드
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.point = :point WHERE u.id = :id")
    int updatePointById(@Param("id") Long id, @Param("point") int point);


}
