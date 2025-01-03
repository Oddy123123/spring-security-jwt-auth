package com.example.myjwt.controller;

import com.example.myjwt.dto.EmployeeLoginRequestDto;
import com.example.myjwt.dto.LoginResponseDto;
import com.example.myjwt.entity.Admin;
import com.example.myjwt.entity.User;
import com.example.myjwt.security.JwtGenerator;
import com.example.myjwt.security.UserTypes;
import com.example.myjwt.security.user.details.AdminUserDetails;
import com.example.myjwt.security.user.details.EmployeeUserDetails;
import com.example.myjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/employee")
public class EmployeeAuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private @Qualifier("employeeAuthenticationManager") AuthenticationManager employeeAuthenticationManager;
    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody EmployeeLoginRequestDto requestDto) {
        try {
            Authentication authentication = employeeAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
            User user = ((EmployeeUserDetails) authentication.getPrincipal()).getUser();
            String token = jwtGenerator.generateToken(user.getEmail(), UserTypes.EMPLOYEE.getType(), user.getRoles());
            return new ResponseEntity<>(new LoginResponseDto("Login successful", token), HttpStatusCode.valueOf(200));
        } catch (AuthenticationException ex){
            return new ResponseEntity<>(new LoginResponseDto("Invalid login credentials"), HttpStatusCode.valueOf(401));
        }
    }

}
