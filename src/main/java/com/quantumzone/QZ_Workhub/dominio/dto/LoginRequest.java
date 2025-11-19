package com.quantumzone.QZ_Workhub.dominio.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
