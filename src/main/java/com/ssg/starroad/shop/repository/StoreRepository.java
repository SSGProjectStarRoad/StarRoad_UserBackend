package com.ssg.starroad.shop.repository;

import com.ssg.starroad.shop.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // complexShoppingmallId에 해당하는 Store 목록을 조회하는 메서드
    // Optional<List<Store>>: 결과가 없을 경우 빈 Optional을 반환하고, 결과가 있을 경우 Store의 리스트를 Optional로 감싸서 반환
    Optional<List<Store>> findByComplexShoppingmallId(Long id);
    Optional<Store> findById(Long id);

}