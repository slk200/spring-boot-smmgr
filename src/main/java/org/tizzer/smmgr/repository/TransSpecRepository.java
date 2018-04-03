package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.TransSpec;

import java.util.List;

public interface TransSpecRepository extends JpaRepository<TransSpec, Long> {
    List<TransSpec> findAllBySerialNo(Long serialNo);
}
