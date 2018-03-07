package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Employee;
import org.tizzer.smmgr.model.request.LoginRequestDto;
import org.tizzer.smmgr.model.request.LogoutRequestDto;
import org.tizzer.smmgr.model.response.LoginResponseDto;
import org.tizzer.smmgr.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/smmgr")
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public LoginResponseDto login(HttpServletRequest request, LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        try {
            Employee employee = userRepository.findOne(loginRequestDto.getAccount());
            if (employee != null) {
                if (employee.getPassword().equals(loginRequestDto.getPassword())) {
                    if (!employee.getLogin()) {
                        //TODO for prod
//                        userRepository.updateLoginState(true, employee.getAccount());
                        loginResponseDto.setAdmin(employee.getAdmin());
                        loginResponseDto.setCode(ResultCode.OK);
                    } else {
                        loginResponseDto.setMessage("该用户已经在线");
                        loginResponseDto.setCode(ResultCode.ERROR);
                    }
                } else {
                    loginResponseDto.setMessage("密码错误");
                    loginResponseDto.setCode(ResultCode.ERROR);
                }
            } else {
                loginResponseDto.setMessage("账号不存在");
                loginResponseDto.setCode(ResultCode.ERROR);
            }
        } catch (Exception e) {
            //TODO 异常处理
            e.printStackTrace();
            loginResponseDto.setMessage(e.getMessage());
            loginResponseDto.setCode(ResultCode.ERROR);
        }
        return loginResponseDto;
    }

    @PostMapping("/logout")
    public void logout(LogoutRequestDto logoutRequestDto) {
        userRepository.updateLoginState(false, logoutRequestDto.getAccount());
    }
}
