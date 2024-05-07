package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findAllByStoreId(Long id);
}
