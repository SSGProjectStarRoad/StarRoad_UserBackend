package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
}
