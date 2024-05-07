package com.ssg.starroad.shop.controller;

import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/store")
@RestController
@RequiredArgsConstructor
public class StoreController {
    private static final Logger log = LoggerFactory.getLogger(StoreController.class);
    private final StoreService storeService;


    @GetMapping("/종합쇼핑몰 넘버 /~~/~~/~~")
    public ResponseEntity<List<StoreDTO>>StoreList(){

        List<StoreDTO> storeDTOList =storeService.searchStoreList(1L);
        log.info(storeDTOList.toString());
        return ResponseEntity.ok(storeDTOList);
    }


}
