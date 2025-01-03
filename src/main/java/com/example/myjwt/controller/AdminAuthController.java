package com.example.myjwt.controller;

import com.example.myjwt.dto.AdminLoginRequestDto;
import com.example.myjwt.dto.LoginResponseDto;
import com.example.myjwt.entity.Admin;
import com.example.myjwt.security.JwtGenerator;
import com.example.myjwt.security.UserTypes;
import com.example.myjwt.security.user.details.AdminUserDetails;
import com.example.myjwt.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/admin")
public class AdminAuthController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private @Qualifier("adminAuthenticationManager") AuthenticationManager adminAuthenticationManager;
    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody Admin admin) {
        adminService.createAdmin(admin);
        return ResponseEntity.ok("Admin created successfully");
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody AdminLoginRequestDto requestDto) {
        try {
            Authentication authentication = adminAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
            Admin admin = ((AdminUserDetails) authentication.getPrincipal()).getAdmin();
            String token = jwtGenerator.generateToken(admin.getUsername(), UserTypes.ADMIN.getType(), admin.getRoles());
            return new ResponseEntity<>(new LoginResponseDto("Login successful", token), HttpStatusCode.valueOf(200));
        } catch (AuthenticationException ex){
            return new ResponseEntity<>(new LoginResponseDto("Invalid login credentials"), HttpStatusCode.valueOf(401));
        }
    }

}
