package com.ssg.starroad.review.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ReviewSelection {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String shopType;
    private String content;
}
