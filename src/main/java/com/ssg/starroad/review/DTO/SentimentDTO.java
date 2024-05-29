package com.ssg.starroad.review.DTO;

import com.ssg.starroad.review.enums.ConfidenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SentimentDTO {
    ConfidenceType documentConfidence;

    List<SentimentDetailDTO> sentimentDetailDTOList;
}
