package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tizzer.smmgr.entity.TradeRecord;

public interface TradeRecordRepository extends JpaRepository<TradeRecord, String>, JpaSpecificationExecutor<TradeRecord> {
    @Query(value = "select original_serial from trade_record tr where tr.original_serial=:originalSerial", nativeQuery = true)
    String isExist(@Param("originalSerial") String originalSerial);
}
