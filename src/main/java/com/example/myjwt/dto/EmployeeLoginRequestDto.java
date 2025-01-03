package com.example.myjwt.dto;

import lombok.Data;

@Data
public class EmployeeLoginRequestDto {
    private String email;
    private String password;
}
