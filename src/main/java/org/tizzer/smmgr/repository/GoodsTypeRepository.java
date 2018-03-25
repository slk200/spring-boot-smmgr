package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tizzer.smmgr.entity.GoodsType;

public interface GoodsTypeRepository extends JpaRepository<GoodsType, Integer> {
    @Query(value = "SELECT name FROM GoodsType g WHERE g.id=:id", nativeQuery = true)
    String getName(@Param("id") Integer id);
}
