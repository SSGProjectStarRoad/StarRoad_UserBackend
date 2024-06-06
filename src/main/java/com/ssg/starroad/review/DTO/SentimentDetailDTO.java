package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.enums.ConfidenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SentimentDetailDTO {

    private String content;
    private int offset;
    private int length;

    private ConfidenceType confidence;

    private int highlightOffset;
    private int highlightLength;
}
