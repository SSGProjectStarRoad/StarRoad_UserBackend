package com.ssg.starroad.shop.repository;

import com.ssg.starroad.shop.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<List<Store>> findByComplexShoppingmallId(Long id);


}
