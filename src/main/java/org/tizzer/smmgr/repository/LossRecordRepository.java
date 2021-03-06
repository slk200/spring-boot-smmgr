package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tizzer.smmgr.entity.LossRecord;

public interface LossRecordRepository extends JpaRepository<LossRecord, String>, JpaSpecificationExecutor<LossRecord> {
}
