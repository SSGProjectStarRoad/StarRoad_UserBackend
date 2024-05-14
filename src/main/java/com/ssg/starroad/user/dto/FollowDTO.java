package com.ssg.starroad.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FollowDTO {
    private Long id;
    private String nickname;
    private String email;
    private String profileImgUrl;
}
