package com.ssg.starroad.review.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.starroad.review.DTO.ReviewReceiptDTO;
import com.ssg.starroad.review.entity.ReviewReceipt;
import com.ssg.starroad.review.repository.ReviewReceiptRepository;
import com.ssg.starroad.review.service.ReviewReceiptService;
import com.ssg.starroad.shop.repository.StoreRepository;
import com.ssg.starroad.shop.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewReceiptServiceImpl implements ReviewReceiptService {

    private final ReviewReceiptRepository reviewReceiptRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;

    @Override
    public ReviewReceiptDTO saveReviewReceipt(ReviewReceiptDTO reviewReceiptDTO) {
        // DTO를 엔티티로 변환
        ReviewReceipt reviewReceipt = ReviewReceipt.builder().shopName(reviewReceiptDTO.getShopName()).paymentType(reviewReceiptDTO.getPaymentType()).approvalNumber(reviewReceiptDTO.getApprovalNumber()).purchaseDate(reviewReceiptDTO.getPurchaseDate()).build();

        // 데이터 저장
        ReviewReceipt savedReviewReceipt = reviewReceiptRepository.save(reviewReceipt);

        // 저장된 엔티티를 다시 DTO로 변환하여 반환
        return new ReviewReceiptDTO(savedReviewReceipt.getShopName(), savedReviewReceipt.getPaymentType(), savedReviewReceipt.getApprovalNumber(), savedReviewReceipt.getPurchaseDate());
    }

    @Override
    public ReviewReceiptDTO processAndSaveOcrResult(String ocrResultJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewReceiptDTO resultDTO = null;
        try {
            JsonNode rootNode = objectMapper.readTree(ocrResultJson);
            JsonNode storeInfoNode = rootNode.path("images").get(0).path("receipt").path("result").path("storeInfo");
            JsonNode paymentInfoNode = rootNode.path("images").get(0).path("receipt").path("result").path("paymentInfo");

            // 상점 이름 추출
            String shopName = storeService.findStoreName(storeInfoNode.path("name").path("text").asText());
            storeRepository.findByName(shopName).orElse(null);
//            String shopName = "노브랜드";
            // 지점명 추출 - 필요하다면
            String subName = storeInfoNode.path("subName").path("text").asText();
            // 주소 추출 - 배열의 첫 번째 주소 추출
            String address = storeInfoNode.path("addresses").path("text").asText();
            // 결제 정보 추출
            String confirmNum = paymentInfoNode.path("confirmNum").path("text").asText();
            String date = paymentInfoNode.path("date").path("text").asText();
            String time = paymentInfoNode.path("time").path("text").asText();
            String cardCompany = paymentInfoNode.path("cardInfo").path("company").path("text").asText(); // 해당 경로 수정 필요 여부 확인

            System.out.println("shopNmae : " + shopName);
            System.out.println("subName : " + subName);
            System.out.println("address : " + address);
            System.out.println("confimNum : " + confirmNum);
            System.out.println("Date : " + date);
            System.out.println("Time : " + time);

            // 날짜와 시간 형식을 정의합니다.
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            // 문자열로부터 LocalDate와 LocalTime을 파싱합니다.
            LocalDate parsedDate = LocalDate.parse(date.replace(" ", ""), dateFormatter);
            LocalTime parsedTime = LocalTime.parse(time.replace(" ", ""), timeFormatter);
            System.out.println("parsedDate : " + parsedDate);
            System.out.println("parsedTime : " + parsedTime);

            // LocalDate와 LocalTime을 합쳐 LocalDateTime 객체를 생성합니다.
            LocalDateTime purchaseDate = LocalDateTime.of(parsedDate, parsedTime);


            // DTO를 엔티티로 변환
            ReviewReceipt reviewReceipt = ReviewReceipt.builder().shopName(shopName)
//                    .subName(subName) // 지점명 저장 - 필요한 경우
//                    .address(address) // 주소 저장 - 필요한 경우
                    .approvalNumber(confirmNum).purchaseDate(purchaseDate).paymentType(cardCompany).build();

            // 데이터 저장
            ReviewReceipt savedReviewReceipt = reviewReceiptRepository.save(reviewReceipt);
            System.out.println("saveReviewReceipt : " + savedReviewReceipt.toString());
            // 저장된 엔티티를 다시 DTO로 변환하여 반환
            resultDTO = new ReviewReceiptDTO(savedReviewReceipt.getShopName(),
//                    savedReviewReceipt.getSubName(), // 지점명 반환 - 필요한 경우
//                    savedReviewReceipt.getAddress(), // 주소 반환 - 필요한 경우
                    savedReviewReceipt.getPaymentType(), savedReviewReceipt.getApprovalNumber(), savedReviewReceipt.getPurchaseDate());
            System.out.println("resultDTO : " + resultDTO.toString());
            return resultDTO;
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 발생 시 적절한 예외를 던지거나 처리
            throw new RuntimeException("OCR 처리 중 오류 발생", e);
        }
    }


    @Override
    public Optional<ReviewReceiptDTO> getReviewReceiptById(Long id) {
        return reviewReceiptRepository.findById(id).map(reviewReceipt -> new ReviewReceiptDTO(reviewReceipt.getShopName(), reviewReceipt.getPaymentType(), reviewReceipt.getApprovalNumber(), reviewReceipt.getPurchaseDate()));
    }

    @Override
    public List<ReviewReceiptDTO> getAllReviewReceipts() {
        return reviewReceiptRepository.findAll().stream().map(reviewReceipt -> new ReviewReceiptDTO(reviewReceipt.getShopName(), reviewReceipt.getPaymentType(), reviewReceipt.getApprovalNumber(), reviewReceipt.getPurchaseDate())).collect(Collectors.toList());
    }

    @Override
    public ReviewReceiptDTO updateReviewReceipt(Long id, ReviewReceiptDTO reviewReceiptDetailsDTO) {
        ReviewReceipt reviewReceipt = reviewReceiptRepository.findById(id).orElseThrow(() -> new RuntimeException("ReviewReceipt not found for this id :: " + id));

        reviewReceipt.setShopName(reviewReceiptDetailsDTO.getShopName());
        reviewReceipt.setPaymentType(reviewReceiptDetailsDTO.getPaymentType());
        reviewReceipt.setApprovalNumber(reviewReceiptDetailsDTO.getApprovalNumber());
        reviewReceipt.setPurchaseDate(reviewReceiptDetailsDTO.getPurchaseDate());

        ReviewReceipt updatedReviewReceipt = reviewReceiptRepository.save(reviewReceipt);

        return new ReviewReceiptDTO(updatedReviewReceipt.getShopName(), updatedReviewReceipt.getPaymentType(), updatedReviewReceipt.getApprovalNumber(), updatedReviewReceipt.getPurchaseDate());
    }

    @Override
    public void deleteReviewReceipt(Long id) {
        ReviewReceipt reviewReceipt = reviewReceiptRepository.findById(id).orElseThrow(() -> new RuntimeException("ReviewReceipt not found for this id :: " + id));
        reviewReceiptRepository.delete(reviewReceipt);
    }
}
