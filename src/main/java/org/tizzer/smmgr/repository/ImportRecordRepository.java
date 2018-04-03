package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tizzer.smmgr.entity.ImportRecord;

public interface ImportRecordRepository extends JpaRepository<ImportRecord, Long>, JpaSpecificationExecutor<ImportRecord> {
}
