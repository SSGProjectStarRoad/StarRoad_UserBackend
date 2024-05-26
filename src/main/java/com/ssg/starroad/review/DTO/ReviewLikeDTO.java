package com.ssg.starroad.review.DTO;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewLikeDTO {
    private boolean isLiked;
    private long likeCount;

}
