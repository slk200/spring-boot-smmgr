package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tizzer.smmgr.entity.BookSpec;

import java.util.List;

public interface BookSpecRepository extends JpaRepository<BookSpec, Long> {
    List<BookSpec> findAllBySerialNo(Long serialNo);
}
