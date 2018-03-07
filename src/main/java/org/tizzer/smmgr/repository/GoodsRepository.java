package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tizzer.smmgr.entity.Goods;

public interface GoodsRepository extends JpaRepository<Goods, Long>,JpaSpecificationExecutor<Goods> {
}
