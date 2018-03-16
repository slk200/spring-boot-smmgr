package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.Insider;

public interface InsiderRepository extends JpaRepository<Insider, String> {
}
