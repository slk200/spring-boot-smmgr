package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.common.LogLevel;
import org.tizzer.smmgr.common.Logcat;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Employee;
import org.tizzer.smmgr.entity.Store;
import org.tizzer.smmgr.model.request.QueryAllEmployeeRequestDto;
import org.tizzer.smmgr.model.request.QuerySomeEmployeeRequestDto;
import org.tizzer.smmgr.model.request.SaveEmployeeRequestDto;
import org.tizzer.smmgr.model.request.UpdateEmployeeRequestDto;
import org.tizzer.smmgr.model.response.*;
import org.tizzer.smmgr.repository.EmployeeRepository;
import org.tizzer.smmgr.repository.StoreRepository;
import org.tizzer.smmgr.utils.TimeUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/smmgr")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    StoreRepository storeRepository;

    /**
     * 查询所有员工
     *
     * @param queryAllEmployeeRequestDto
     * @return
     */
    @PostMapping(path = "/query/employee/all")
    public ResultListResponse<QueryAllEmployeeResponseDto> queryAllEmployee(QueryAllEmployeeRequestDto queryAllEmployeeRequestDto) {
        ResultListResponse<QueryAllEmployeeResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(queryAllEmployeeRequestDto.getCurrentPage(), queryAllEmployeeRequestDto.getPageSize());
            Page<Employee> page = employeeRepository.findAll(pageable);
            for (Employee employee : page.getContent()) {
                QueryAllEmployeeResponseDto queryAllEmployeeResponseDto = new QueryAllEmployeeResponseDto();
                queryAllEmployeeResponseDto.setStaffNo(employee.getStaffNo());
                queryAllEmployeeResponseDto.setName(employee.getName());
                queryAllEmployeeResponseDto.setPhone(employee.getPhone());
                queryAllEmployeeResponseDto.setAddress(employee.getAddress());
                queryAllEmployeeResponseDto.setAdmin(employee.getAdmin());
                queryAllEmployeeResponseDto.setCreateAt(employee.getCreateAt());
                queryAllEmployeeResponseDto.setStore(employee.getStore().getName());
                queryAllEmployeeResponseDto.setState(employee.getEnable());
                res.setData(queryAllEmployeeResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCurrentPage(queryAllEmployeeRequestDto.getCurrentPage());
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.ERROR);
        }
        return res;
    }

    /**
     * 查询满足条件的所有员工
     *
     * @param querySomeEmployeeRequestDto
     * @return
     */
    @PostMapping(path = "/query/employee/some")
    public ResultListResponse<QuerySomeEmployeeResponseDto> queryAllEmployee(QuerySomeEmployeeRequestDto querySomeEmployeeRequestDto) {
        ResultListResponse<QuerySomeEmployeeResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(querySomeEmployeeRequestDto.getCurrentPage(), querySomeEmployeeRequestDto.getPageSize());
            Specification<Employee> specification = new Specification<Employee>() {
                @Override
                public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (querySomeEmployeeRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), TimeUtil.startOfDay(querySomeEmployeeRequestDto.getStartDate())));
                    }
                    if (querySomeEmployeeRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("createAt"), TimeUtil.endOfDay(querySomeEmployeeRequestDto.getEndDate())));
                    }
                    if (!querySomeEmployeeRequestDto.getKeyWord().equals("")) {
                        predicates.add(cb.or(cb.like(root.get("staffNo"), "%" + querySomeEmployeeRequestDto.getKeyWord() + "%"),
                                cb.like(root.get("name"), "%" + querySomeEmployeeRequestDto.getKeyWord() + "%"),
                                cb.like(root.get("phone"), "%" + querySomeEmployeeRequestDto.getKeyWord() + "%"),
                                cb.like(root.get("address"), "%" + querySomeEmployeeRequestDto.getKeyWord() + "%")));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
                    }
                    return null;
                }
            };
            Page<Employee> page = employeeRepository.findAll(specification, pageable);
            for (Employee employee : page.getContent()) {
                QuerySomeEmployeeResponseDto querySomeEmployeeResponseDto = new QuerySomeEmployeeResponseDto();
                querySomeEmployeeResponseDto.setStaffNo(employee.getStaffNo());
                querySomeEmployeeResponseDto.setName(employee.getName());
                querySomeEmployeeResponseDto.setPhone(employee.getPhone());
                querySomeEmployeeResponseDto.setAddress(employee.getAddress());
                querySomeEmployeeResponseDto.setAdmin(employee.getAdmin());
                querySomeEmployeeResponseDto.setCreateAt(employee.getCreateAt());
                querySomeEmployeeResponseDto.setStore(employee.getStore().getName());
                querySomeEmployeeResponseDto.setState(employee.getEnable());
                res.setData(querySomeEmployeeResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCurrentPage(querySomeEmployeeRequestDto.getCurrentPage());
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.ERROR);
        }
        return res;
    }

    /**
     * 保存员工
     *
     * @param saveEmployeeRequestDto
     * @return
     */
    @PostMapping(path = "/save/employee")
    public SaveEmployeeResponseDto saveEmployee(SaveEmployeeRequestDto saveEmployeeRequestDto) {
        SaveEmployeeResponseDto saveEmployeeResponseDto = new SaveEmployeeResponseDto();
        try {
            Store store = storeRepository.findOne(saveEmployeeRequestDto.getStoreId());
            Employee employee = new Employee();
            employee.setStaffNo(saveEmployeeRequestDto.getStaffNo());
            employee.setPassword(saveEmployeeRequestDto.getPassword());
            employee.setName(saveEmployeeRequestDto.getName());
            employee.setPhone(saveEmployeeRequestDto.getPhone());
            employee.setAddress(saveEmployeeRequestDto.getAddress());
            employee.setAdmin(saveEmployeeRequestDto.getAdmin());
            System.out.println(saveEmployeeRequestDto.getAdmin());
            employee.setCreateAt(new Date());
            employee.setStore(store);
            employee.setEnable(true);
            employeeRepository.save(employee);
            saveEmployeeResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            saveEmployeeResponseDto.setMessage(e.getMessage());
            saveEmployeeResponseDto.setCode(ResultCode.ERROR);
        }
        return saveEmployeeResponseDto;
    }

    /**
     * 修改员工资料
     *
     * @param updateEmployeeRequestDto
     * @return
     */
    @PostMapping(path = "/update/employee")
    public UpdateEmployeeResponseDto updateEmployee(UpdateEmployeeRequestDto updateEmployeeRequestDto) {
        UpdateEmployeeResponseDto updateEmployeeResponseDto = new UpdateEmployeeResponseDto();
        try {
            employeeRepository.updateEmployee(updateEmployeeRequestDto.getStaffNo(), updateEmployeeRequestDto.getPhone(), updateEmployeeRequestDto.getAddress(), updateEmployeeRequestDto.getAdmin(), updateEmployeeRequestDto.getEnable());
            updateEmployeeResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            updateEmployeeResponseDto.setMessage(e.getMessage());
            updateEmployeeResponseDto.setCode(ResultCode.ERROR);
        }
        return updateEmployeeResponseDto;
    }
}
