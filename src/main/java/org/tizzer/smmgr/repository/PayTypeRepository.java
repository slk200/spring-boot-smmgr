package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tizzer.smmgr.entity.PayType;

import java.util.List;

public interface PayTypeRepository extends JpaRepository<PayType, Integer> {
    @Query(value = "select name from pay_type", nativeQuery = true)
    List<String> findAllPayType();
}
