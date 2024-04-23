package com.ssg.starroad.board.entity;

import com.ssg.starroad.board.enums.BoardCategory;
import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardCategory category;
}
