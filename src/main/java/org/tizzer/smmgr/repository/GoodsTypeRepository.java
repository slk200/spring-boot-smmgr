package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tizzer.smmgr.entity.GoodsType;

public interface GoodsTypeRepository extends JpaRepository<GoodsType, Integer> {
    @Query(value = "from GoodsType where id=?1")
    String getName(Integer id);
}
