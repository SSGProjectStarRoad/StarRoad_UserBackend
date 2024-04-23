package com.ssg.starroad.shop.entity;

import com.ssg.starroad.user.entity.Manager;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class ComplexShoppingmall {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;
    private String name;
    private String address;
}
