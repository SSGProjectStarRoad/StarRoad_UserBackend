package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.entity.ReviewSelection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewSelectionDTO {
    private Long id;
    private String shopType;
    private String content;
    private String essential;
    private String optional;

    public ReviewSelectionDTO(String shopType, String content, String essential, String optional) {
        this.shopType = shopType;
        this.content = content;
        this.essential = essential;
        this.optional = optional;
    }

    // Entity를 DTO로 변환하는 메소드
    public static ReviewSelectionDTO fromEntity(ReviewSelection reviewSelection) {
        ReviewSelectionDTO dto = new ReviewSelectionDTO();
        dto.setId(reviewSelection.getId());
        dto.setShopType(reviewSelection.getShopType());
        dto.setContent(reviewSelection.getContent());
        return dto;
    }
}
