package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.ReviewKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewKeywordRepository  extends JpaRepository<ReviewKeyword, Long> {
    @Query("SELECT rk FROM ReviewKeyword rk WHERE rk.storeType = :storeType")
    List<ReviewKeyword> findAllByStoreType(@Param("storeType") String storeType);

}
