package com.ssg.starroad.shop.entity;

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
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "complex_shoppingmall_id")
    private ComplexShoppingmall complexShoppingmall;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User managerId;

    @Column(unique = true)
    private String name;
    private String storeType;
    private String imagePath;
    private String contents;
    private int floor;
    private String operatingTime; // hh:mm ~ hh:mm format
    private String contactNumber;
}
