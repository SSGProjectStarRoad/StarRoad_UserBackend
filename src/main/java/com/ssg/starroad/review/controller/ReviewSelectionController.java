package com.ssg.starroad.review.controller;

import com.ssg.starroad.review.DTO.ReviewSelectionDTO;
import com.ssg.starroad.review.service.ReviewSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review-selections")
@RequiredArgsConstructor
public class ReviewSelectionController {

    private final ReviewSelectionService reviewSelectionService;

    @PostMapping("/selection")
    public List<String> getReviewSelectionsByShopName(@RequestBody String shopName) {
        return reviewSelectionService.findReviewSelectionsByShopName(shopName);
    }
}
