package com.ssg.starroad.review.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.starroad.review.DTO.ReviewSelectionDTO;
import com.ssg.starroad.review.entity.ReviewSelection;
import com.ssg.starroad.review.repository.ReviewSelectionRepository;
import com.ssg.starroad.review.service.ReviewSelectionService;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.shop.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewSelectionServiceImpl implements ReviewSelectionService {

    private final ReviewSelectionRepository reviewSelectionRepository;
    private final StoreRepository storeRepository;

    @Override
    public ReviewSelectionDTO saveReviewSelection(ReviewSelectionDTO reviewSelectionDTO) {
        // essential과 optional 설문 항목을 ','를 기준으로 합치기
        String content = reviewSelectionDTO.getEssential() + "," + reviewSelectionDTO.getOptional();

        ReviewSelection reviewSelection = ReviewSelection.builder()
                .shopType(reviewSelectionDTO.getShopType())
                .content(reviewSelectionDTO.getContent())
                .build();

        ReviewSelection savedEntity = reviewSelectionRepository.save(reviewSelection);
        return ReviewSelectionDTO.fromEntity(savedEntity);
    }

    @Override
    public ReviewSelectionDTO findReviewSelectionById(Long id) {
        return reviewSelectionRepository.findById(id)
                .map(ReviewSelectionDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("ReviewSelection not found for the id: " + id));
    }

    @Override
    public List<ReviewSelectionDTO> findAllReviewSelections() {
        return reviewSelectionRepository.findAll()
                .stream()
                .map(ReviewSelectionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewSelectionDTO updateReviewSelection(Long id, ReviewSelectionDTO reviewSelectionDto) {
        ReviewSelection existingReviewSelection = reviewSelectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ReviewSelection not found for the id: " + id));

        ReviewSelection reviewSelectionToUpdate = ReviewSelection.builder()
                .id(existingReviewSelection.getId()) // 기존 ID 유지
                .shopType(reviewSelectionDto.getShopType())
                .content(reviewSelectionDto.getContent())
                .build();

        ReviewSelection updatedEntity = reviewSelectionRepository.save(reviewSelectionToUpdate);
        return ReviewSelectionDTO.fromEntity(updatedEntity);
    }

    @Override
    public void deleteReviewSelection(Long id) {
        ReviewSelection reviewSelection = reviewSelectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ReviewSelection not found for the id: " + id));
        reviewSelectionRepository.delete(reviewSelection);
    }

    //    @Override
//    public List<String> findReviewSelectionsByShopName(String shopName) {
//
//        System.out.println("Searching for store type with name: " + shopName);
//        String storeType = storeRepository.findStoreTypeByShopName(shopName);
//        System.out.println("Found store type: " + storeType);
//
//        if (storeType == null) {
//            throw new IllegalArgumentException("Store not found for the shop name: " + shopName);
//        }
//        List<String> reviewSelections = reviewSelectionRepository.findByShopType(storeType)
//                .stream()
//                .map(ReviewSelection::getContent)
//                .collect(Collectors.toList());
//        System.out.printf(reviewSelections.toString());
//        return reviewSelections;
//    }
    @Override
    public List<String> findReviewSelectionsByShopName(String shopNameJson) {
        System.out.println("Parsing JSON input: " + shopNameJson);

        ObjectMapper objectMapper = new ObjectMapper();
        String shopName;
        try {
            JsonNode jsonNode = objectMapper.readTree(shopNameJson);
            shopName = jsonNode.get("shopName").asText();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format: " + shopNameJson, e);
        }

        System.out.println("Searching for store with name: " + shopName);
        Store store = storeRepository.findByName(shopName)
                .orElseThrow(() -> new IllegalArgumentException("Store not found for the shop name: " + shopName));
        String storeType = store.getStoreType();
        System.out.println("Found store type: " + storeType);

        List<String> reviewSelections = reviewSelectionRepository.findByShopType(storeType)
                .stream()
                .map(ReviewSelection::getContent)
                .collect(Collectors.toList());
        System.out.println(reviewSelections.toString());
        return reviewSelections;
    }

}
