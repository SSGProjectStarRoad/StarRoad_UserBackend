package com.ssg.starroad.shop.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.user.entity.Manager;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "complex_shoppingmall_id")
    private ComplexShoppingmall complexShoppingmall;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Column(unique = true)
    private String name; // 입점매장 이름
    private String storeType; // 입점 매장 카테고리(TYPE)
    private String imagePath; // 매장 로고
    private String contents; // 매장 소개글
    private int floor; // 매장 층수
    private String operatingTime; // hh:mm ~ hh:mm format
    private String contactNumber; // 연락처
    private String storeGuideMap; // 가게 안내도 이미지
    private Long ReviewCount; // 리뷰 수

        public StoreDTO toDTO(){
            return StoreDTO.builder()
                    .name(name)
                    .floor(floor)
                    .storeGuideMap(storeGuideMap)
                    .contactNumber(contactNumber)
                    .imagePath(imagePath)
                    .storeType(storeType)
                    .build();
        }

}
