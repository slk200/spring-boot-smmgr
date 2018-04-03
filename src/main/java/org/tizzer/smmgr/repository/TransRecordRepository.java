package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tizzer.smmgr.entity.TransRecord;

public interface TransRecordRepository extends JpaRepository<TransRecord, Long>, JpaSpecificationExecutor<TransRecord> {
}
