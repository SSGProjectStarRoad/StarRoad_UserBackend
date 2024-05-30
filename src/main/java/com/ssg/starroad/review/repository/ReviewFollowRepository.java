package com.ssg.starroad.review.repository;

import com.ssg.starroad.user.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewFollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f.toUser.id FROM Follow f WHERE f.fromUser.id = :fromUserId")
    List<Long> findToUserIdsByFromUserId(@Param("fromUserId") Long fromUserId);


}
