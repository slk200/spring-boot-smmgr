package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.LossSpec;

import java.util.List;

public interface LossSpecRepository extends JpaRepository<LossSpec, Long> {
    List<LossSpec> findAllBySerialNo(String serialNo);
}
