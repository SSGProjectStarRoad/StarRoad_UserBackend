package com.ssg.starroad.user.repository;

import com.ssg.starroad.user.entity.Follow;
import com.ssg.starroad.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.fromUser.id = :userId")
    long countByFromUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.toUser.id = :userId")
    long countByToUserId(@Param("userId") Long userId);

    @Query("SELECT f.toUser FROM Follow f WHERE f.fromUser.id = :fromUserId")
    List<User> findToUsersByFromUserId(@Param("fromUserId") Long fromUserId);

    @Query("SELECT f.fromUser FROM Follow f WHERE f.toUser.id = :toUserId")
    List<User> findFromUsersByToUserId(@Param("toUserId") Long toUserId);

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.fromUser.id = :fromUserId AND f.toUser.id = :toUserId")
    void deleteByFromUserIdAndToUserId(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);

    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
