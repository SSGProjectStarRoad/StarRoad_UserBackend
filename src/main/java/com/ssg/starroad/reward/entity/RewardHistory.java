package com.ssg.starroad.reward.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long rewardId;

}
