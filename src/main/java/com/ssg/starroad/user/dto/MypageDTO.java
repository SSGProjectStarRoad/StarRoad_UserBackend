package com.ssg.starroad.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MypageDTO {
    private String nickname;
    private String profileImg;
    private int reviewExp;
    private String name;
    private int point;
}
