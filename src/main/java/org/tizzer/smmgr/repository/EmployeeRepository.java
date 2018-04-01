package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tizzer.smmgr.entity.Employee;

import javax.transaction.Transactional;

public interface EmployeeRepository extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee> {

    Employee findByStaffNoAndPassword(String staffNo, String password);

    @Modifying
    @Transactional
    @Query(value = "update employee e set e.phone=:phone,e.address=:address,e.is_admin=:admin,e.is_enable=:enable where e.staff_no=:staffNo", nativeQuery = true)
    void updateEmployee(@Param("staffNo") String staffNo, @Param("phone") String phone, @Param("address") String address, @Param("admin") boolean admin, @Param("enable") boolean enable);

}
