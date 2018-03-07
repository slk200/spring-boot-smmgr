package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tizzer.smmgr.entity.Employee;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee> {
    @Modifying
    @Transactional
    @Query(value = "update User set login=?1 where account=?2", nativeQuery = true)
    void updateLoginState(boolean state, String account);
}
