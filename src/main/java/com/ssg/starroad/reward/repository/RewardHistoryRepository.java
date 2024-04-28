package com.ssg.starroad.reward.repository;

import com.ssg.starroad.reward.entity.RewardHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {
}
