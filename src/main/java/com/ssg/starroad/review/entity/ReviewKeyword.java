package com.ssg.starroad.review.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ReviewKeyword {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
}
