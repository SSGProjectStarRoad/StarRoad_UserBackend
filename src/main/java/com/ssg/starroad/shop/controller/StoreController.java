package com.ssg.starroad.shop.controller;

import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.DTO.StoreWithReviewDTO;
import com.ssg.starroad.shop.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// "/store"로 시작하는 모든 요청에 대한 처리를 위한 컨트롤러 클래스입니다.
@RequestMapping("/store")
@RestController
@RequiredArgsConstructor
public class StoreController {
    private static final Logger log = LoggerFactory.getLogger(StoreController.class);
    private final StoreService storeService;

    // 메인 매장 목록을 검색하는 요청을 처리하는 메소드입니다.
    @GetMapping("/main")
    public ResponseEntity<List<StoreDTO>> StoreSearch() {
        // 매장 서비스를 통해 메인 매장 목록을 검색합니다.
        List<StoreDTO> storeDTOList = storeService.searchStoreList(1L);
        log.info(storeDTOList.toString());
        // 검색된 메인 매장 목록을 응답으로 반환합니다.
        return ResponseEntity.ok(storeDTOList);
    }

    // 특정 매장과 그에 대한 리뷰를 함께 조회하는 요청을 처리하는 메소드입니다.
    @GetMapping("/{id}/reviews")
    public ResponseEntity<StoreWithReviewDTO> StoreGetWithReviewById(@PathVariable Long id) {
        try {
            // 매장 서비스를 통해 특정 매장과 그에 대한 리뷰를 함께 조회합니다.
            StoreWithReviewDTO storeWithReviewDTO = storeService.findStoreWithReview(id);
            // 조회된 매장과 리뷰를 응답으로 반환합니다.
            return ResponseEntity.ok(storeWithReviewDTO);
        } catch (RuntimeException e) {
            // 조회 중에 예외가 발생한 경우 404 응답을 반환합니다.
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 매장의 가이드 맵을 조회하는 요청을 처리하는 메소드입니다.
    @GetMapping("/{id}/guidemap")
    public ResponseEntity<StoreDTO> StoreGetGuidemapById(@PathVariable Long id) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        try {
            // 매장 서비스를 통해 특정 매장의 가이드 맵을 조회합니다.
            StoreDTO storeDTO = storeService.findStore(id);
            // 조회된 가이드 맵을 응답으로 반환합니다.
            log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            log.info(storeDTO.toString());
            return ResponseEntity.ok(storeDTO);
        } catch (RuntimeException e) {
            // 조회 중에 예외가 발생한 경우 404 응답을 반환합니다.
            return ResponseEntity.notFound().build();
        }
    }
}
