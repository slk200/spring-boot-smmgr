package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.LossSpec;

public interface LossSpecRepository extends JpaRepository<LossSpec, Long> {
}
