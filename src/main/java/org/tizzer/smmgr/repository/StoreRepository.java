package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tizzer.smmgr.entity.Store;

import javax.transaction.Transactional;

public interface StoreRepository extends JpaRepository<Store, Integer>, JpaSpecificationExecutor<Store> {

    @Modifying
    @Transactional
    @Query(value = "update store s set s.name=:name,s.address=:address where s.id=:id", nativeQuery = true)
    void updateStore(@Param("id") Long id, @Param("name") String name, @Param("address") String address);

}
