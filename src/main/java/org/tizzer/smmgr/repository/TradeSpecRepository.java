package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.TradeSpec;

public interface TradeSpecRepository extends JpaRepository<TradeSpec, String> {
}