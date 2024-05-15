package com.ssg.starroad.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FollowCountDTO {
    private long followerCount;
    private long followingCount;

}
