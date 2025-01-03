package com.example.myjwt.dto;

import lombok.Data;

@Data
public class AdminLoginRequestDto {
    private String username;
    private String password;
}
