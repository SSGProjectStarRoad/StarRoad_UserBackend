package com.ssg.starroad.review.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewSelectionDTO {
    private Long id;
    private String shopType;
    private String content;

    // ReviewSelection 엔티티
    public ReviewSelectionDTO toDTO() {
        return ReviewSelectionDTO.builder()
                .id(id)
                .shopType(shopType)
                .content(content)
                .build();
    }
}
