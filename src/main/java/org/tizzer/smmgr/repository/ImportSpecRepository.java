package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.ImportSpec;

import java.util.List;

public interface ImportSpecRepository extends JpaRepository<ImportSpec, Long> {
    List<ImportSpec> findAllBySerialNo(Long serialNo);
}
