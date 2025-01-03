package com.example.myjwt.controller;

import com.example.myjwt.entity.Admin;
import com.example.myjwt.entity.User;
import com.example.myjwt.repository.RoleRepository;
import com.example.myjwt.security.user.details.AdminUserDetails;
import com.example.myjwt.security.user.details.EmployeeUserDetails;
import com.example.myjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Admin> test(@AuthenticationPrincipal AdminUserDetails adminUserDetails) {
        Admin admin = adminUserDetails.getAdmin();
        return ResponseEntity.ok(admin);
    }

    @GetMapping("employee")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<User> test(@AuthenticationPrincipal EmployeeUserDetails employeeUserDetails) {
        User user = employeeUserDetails.getUser();
        return ResponseEntity.ok(user);
    }


}
