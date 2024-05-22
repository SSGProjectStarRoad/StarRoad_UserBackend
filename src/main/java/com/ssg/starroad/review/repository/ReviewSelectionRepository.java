package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.ReviewSelection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewSelectionRepository extends JpaRepository<ReviewSelection, Long> {
    List<ReviewSelection> findByShopType(String shopType);
}
