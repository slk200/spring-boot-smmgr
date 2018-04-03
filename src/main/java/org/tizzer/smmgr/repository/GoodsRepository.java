package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tizzer.smmgr.entity.Goods;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, String>, JpaSpecificationExecutor<Goods> {
    List<Goods> findAllByUpcLikeOrNameLikeOrSpellLike(String upc, String name, String spell);
}
