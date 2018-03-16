package org.tizzer.smmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tizzer.smmgr.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Query("from Employee e where e.staffNo=:staffNo and e.password=:password")
    Employee findByStaffNoAndPassword(@Param("staffNo") String staffNo, @Param("password") String password);

}
