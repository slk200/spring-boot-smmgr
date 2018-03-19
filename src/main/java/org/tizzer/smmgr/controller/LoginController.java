package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.common.LogLevel;
import org.tizzer.smmgr.common.Logcat;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Employee;
import org.tizzer.smmgr.model.request.LoginRequestDto;
import org.tizzer.smmgr.model.response.LoginResponseDto;
import org.tizzer.smmgr.repository.EmployeeRepository;

@RestController
@RequestMapping(path = "/smmgr")
public class LoginController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public LoginController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping(path = "/login")
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        try {
            Employee employee = employeeRepository.findByStaffNoAndPassword(loginRequestDto.getStaffNo(), loginRequestDto.getPassword());
            if (employee != null) {
                loginResponseDto.setAdmin(employee.isAdmin());
                loginResponseDto.setCode(ResultCode.OK);
            } else {
                loginResponseDto.setMessage("账号或密码错误");
                loginResponseDto.setCode(ResultCode.ERROR);
            }
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            loginResponseDto.setMessage(e.getMessage());
            loginResponseDto.setCode(ResultCode.ERROR);
        }
        return loginResponseDto;
    }

}
