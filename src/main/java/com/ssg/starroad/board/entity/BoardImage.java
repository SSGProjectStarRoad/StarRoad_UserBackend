package com.ssg.starroad.board.entity;

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
public class BoardImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String imagePath;
}
