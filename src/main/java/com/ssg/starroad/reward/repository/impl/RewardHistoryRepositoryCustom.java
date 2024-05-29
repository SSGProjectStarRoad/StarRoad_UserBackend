package com.ssg.starroad.reward.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.starroad.reward.entity.QReward;
import com.ssg.starroad.reward.entity.QRewardHistory;
import com.ssg.starroad.reward.entity.RewardHistory;
import com.ssg.starroad.user.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RewardHistoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public List<Tuple> countRewardHistoryByUserId(Long userId) {
        QRewardHistory rewardHistory = QRewardHistory.rewardHistory;
        QReward reward = QReward.reward;

        return queryFactory
                .select(rewardHistory.rewardId, reward.name ,rewardHistory.id.count(),reward.rewardImagePath)
                .from(rewardHistory)
                .leftJoin(reward).on(rewardHistory.rewardId.eq(reward.id))  // 명확한 조인 조건 사용
                .where(rewardHistory.user.id.eq(userId))
                .groupBy(rewardHistory.rewardId, reward.name)
                .fetch();
    }
}