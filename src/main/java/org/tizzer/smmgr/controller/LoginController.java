package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContext;
import org.tizzer.smmgr.common.Log;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Employee;
import org.tizzer.smmgr.model.request.LoginRequestDto;
import org.tizzer.smmgr.model.response.LoginResponseDto;
import org.tizzer.smmgr.repository.EmployeeRepository;
import org.tizzer.smmgr.utils.MD5Util;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/smmgr")
public class LoginController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public LoginController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * 登录校验
     *
     * @param loginRequestDto
     * @return
     */
    @PostMapping(path = "/login")
    public LoginResponseDto login(HttpServletRequest request, LoginRequestDto loginRequestDto) {
        RequestContext requestContext = new RequestContext(request);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        try {
            Employee employee = employeeRepository.findByStaffNoAndPassword(loginRequestDto.getStaffNo(), MD5Util.encoder(loginRequestDto.getPassword()));
            if (employee != null) {
                if (employee.getEnable()) {
                    loginResponseDto.setStoreId(employee.getStore().getId());
                    loginResponseDto.setAdmin(employee.getAdmin());
                    loginResponseDto.setCode(ResultCode.OK);
                } else {
                    loginResponseDto.setMessage(requestContext.getMessage("msg.login.result.stop"));
                    loginResponseDto.setCode(ResultCode.ERROR);
                }
            } else {
                loginResponseDto.setMessage(requestContext.getMessage("msg.login.result.error"));
                loginResponseDto.setCode(ResultCode.ERROR);
            }
        } catch (Exception e) {
            loginResponseDto.setMessage(e.getMessage());
            loginResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return loginResponseDto;
    }

}
