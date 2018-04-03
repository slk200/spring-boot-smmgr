package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.TradeSpec;

import java.util.List;

public interface TradeSpecRepository extends JpaRepository<TradeSpec, Long> {
    List<TradeSpec> findAllBySerialNo(String serialNo);
}
