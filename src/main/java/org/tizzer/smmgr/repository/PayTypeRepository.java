package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.PayType;

public interface PayTypeRepository extends JpaRepository<PayType, Integer> {
}
