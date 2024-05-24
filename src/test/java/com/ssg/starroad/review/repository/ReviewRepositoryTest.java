package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ReviewRepositoryTest {


    @Autowired
    private ReviewRepository reviewRepository;


    @Test
    public void findAll() {
        List<Review> reviews = reviewRepository.findAllByStoreId(2L).get();

        for (Review review : reviews) {
            System.out.print(review.getId() + " : ");
            System.out.println(review.getCreatedAt());
        }
    }

    @Test
    public void findAll2() {
        List<Review> reviews = reviewRepository.findAll();

        for (Review review : reviews) {
            System.out.print(review.getId() + " : ");
            System.out.println(review.getCreatedAt());
        }
    }

    @Test
    public void findByuserId() {

        System.out.println(reviewRepository.countByUserId(1L));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
    }
}