package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.ReviewSentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewSentimentRepository extends JpaRepository<ReviewSentiment, Long> {

    @Query("SELECT rs FROM ReviewSentiment rs WHERE rs.review.store.id = :storeId AND rs.content LIKE %:keyword%")
    List<ReviewSentiment> findAllByStoreIdAndKeyword(@Param("storeId") Long storeId, @Param("keyword") String keyword);
}



