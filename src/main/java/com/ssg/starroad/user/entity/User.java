package com.ssg.starroad.user.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password; // bcrypt
    private String name;
    @Column(unique = true)
    private String nickname;
    private String imagePath; // profile image
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    private LocalDate birth;
    private String phone; // xxx-xxxx-xxxx
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String provider; // provider + providerId => unique

    private int reviewExp;
    private int point;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;
}
