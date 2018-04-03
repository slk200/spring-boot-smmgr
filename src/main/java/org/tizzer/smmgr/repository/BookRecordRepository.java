package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tizzer.smmgr.entity.BookRecord;

public interface BookRecordRepository extends JpaRepository<BookRecord, Long>, JpaSpecificationExecutor<BookRecord> {
}
