package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.tizzer.smmgr.entity.Insider;

import java.util.List;

public interface InsiderRepository extends JpaRepository<Insider, String>, JpaSpecificationExecutor<Insider> {

    Insider findByPhone(String phone);

    Insider findByCardNoOrPhone(String cardNo, String phone);

    @Query(value = "SELECT type_id FROM Insider GROUP BY type_id", nativeQuery = true)
    List<Integer> findAllUsingType();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Insider i SET i.type_id=:id,i.birth=:birth WHERE i.card_no=:cardNo", nativeQuery = true)
    void updateInsider(@Param("cardNo") String cardNo, @Param("id") Integer id, @Param("birth") String birth);

}
