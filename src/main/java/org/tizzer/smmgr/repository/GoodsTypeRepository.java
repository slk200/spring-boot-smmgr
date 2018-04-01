package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.GoodsType;

public interface GoodsTypeRepository extends JpaRepository<GoodsType, Integer> {
}
