package com.ssg.starroad.reward.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.review.enums.ConfidenceType;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reward extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String rewardImagePath;
}
