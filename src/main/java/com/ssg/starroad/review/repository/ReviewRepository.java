package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
