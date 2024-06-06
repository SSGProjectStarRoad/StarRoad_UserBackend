package com.ssg.starroad.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewKeyword {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;


    @Column(name = "store_type") // 이 부분이 필요할 수 있습니다.
    private String storeType;
}
