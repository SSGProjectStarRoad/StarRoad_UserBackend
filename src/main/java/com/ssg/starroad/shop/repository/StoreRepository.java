package com.ssg.starroad.shop.repository;

import com.ssg.starroad.shop.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // complexShoppingmallId에 해당하는 Store 목록을 조회하는 메서드
    // Optional<List<Store>>: 결과가 없을 경우 빈 Optional을 반환하고, 결과가 있을 경우 Store의 리스트를 Optional로 감싸서 반환
    Optional<List<Store>> findByComplexShoppingmallId(Long id);
    // store 정보를 id를 기반으로 조회해서 가져오는 메서드
// 주어진 id를 기준으로 Store 엔티티를 조회하여 반환합니다. 존재하지 않을 경우 Optional.empty()를 반환합니다.
    Optional<Store> findById(Long id);
    Optional<Store> findByName(String name);
    // shopName을 기준으로 storeType을 조회하는 새로운 메소드
    @Query("SELECT s.storeType FROM Store s WHERE s.name = :name")
    String findStoreTypeByShopName(@Param("name") String name);

}