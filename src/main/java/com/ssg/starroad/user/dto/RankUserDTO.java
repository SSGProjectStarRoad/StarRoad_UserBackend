package com.ssg.starroad.user.dto;

import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.enums.ActiveStatus;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankUserDTO {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String imagePath;
    private int reviewExp;
    private int point;
    private ActiveStatus activeStatus;
    private boolean isFollowed;

    public RankUserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.imagePath = user.getImagePath();
        this.reviewExp = user.getReviewExp();
        this.point = user.getPoint();
        this.activeStatus = user.getActiveStatus();
    }
}
